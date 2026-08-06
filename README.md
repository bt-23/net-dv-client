# dv.net Java client

A Java client for the [DV.net](https://docs.dv.net/en/) merchant API: issue deposit addresses, read
balances, queue withdrawals and handle webhooks.

Runs on **Java 8 or later**. Built on Apache HttpClient 5 and Jackson 2, with no other dependencies.

## Installation

```xml
<dependency>
    <groupId>net.dv.client</groupId>
    <artifactId>net-dv-client</artifactId>
    <version>1.0.7</version>
</dependency>
```

## Getting started

Both the host and the store API key come from the settings of your merchant. The client owns a
pooled HTTP connection manager, so create one instance per store, share it, and close it when your
application shuts down.

```java
try (DvNet dvNet = new DvNet("https://merchant.example.com", "your_store_api_key")) {
    Wallet wallet = dvNet.wallets().create(
            CreateWalletOptions.builder("customer-42")
                    .email("customer@example.com")
                    .amount("10.50")
                    .currency("USD")
                    .build());

    response.sendRedirect(wallet.getPayUrl());
}
```

Every call may throw a checked `DvNetException`. See [Error handling](#error-handling) below.

## Services

### Currencies

```java
List<Currency> currencies = dvNet.currencies().list();
ExtendedCurrencies grouped = dvNet.currencies().listExtended();
CurrencyRate rate = dvNet.currencies().rate("USDT.Tron");
```

`listExtended()` returns the same catalogue grouped by token and by blockchain, which is what you
want when rendering a currency picker.

### Wallets

`create` is idempotent on `storeExternalId`: calling it again for the same customer returns the same
addresses instead of issuing new ones.

```java
Wallet wallet = dvNet.wallets().create(
        CreateWalletOptions.builder("customer-42")
                .email("customer@example.com")
                .ip("203.0.113.10")
                .amount("10.50")
                .currency("USD")
                .build());

for (WalletAddress address : wallet.getAddresses()) {
    System.out.println(address.getCurrencyId() + " -> " + address.getAddress());
}
```

Aggregated hot wallet balances, optionally filtered to the currencies holding at least an amount:

```java
List<WalletSummary> balances = dvNet.wallets().hotBalances("100");
```

### Balances

```java
ExchangeBalances exchange = dvNet.balances().exchange();

List<ProcessingWalletBalance> processing = dvNet.balances().processingWallets(
        Arrays.asList("tron", "ethereum"),
        Collections.singletonList("USDT.Tron"));
```

### Withdrawals

`requestId` is your own identifier and makes the call idempotent, so a retry after a timeout will
not pay out twice.

```java
Withdrawal withdrawal = dvNet.withdrawals().create(
        CreateWithdrawalOptions.builder("TJRabPrwbZy45sbavfcjinPJC18kjpRTv8", "USDT.Tron", "25.00", "payout-77")
                .build());

WithdrawalStatus status = dvNet.withdrawals().get(withdrawal.getId());

dvNet.withdrawals().remove(withdrawal.getId()); // only while still queued
```

### Transactions

```java
List<UnconfirmedTransaction> pending = dvNet.transactions().listUnconfirmed();
```

## Webhooks

DV.net signs each delivery with the SHA-256 hash of the raw body concatenated with your webhook
secret, hex encoded, and sends it in the `X-Sign` header.

Verify against the body **exactly as it arrived**. Parsing and re-serializing changes the bytes and
the signature will no longer match, so read the raw body before any JSON binding runs.

```java
String body = readRawBody(request);
String signature = request.getHeader(Webhooks.SIGNATURE_HEADER);

WebhookEvent event = dvNet.webhooks().constructEvent(body, signature, webhookSecret);

if (event instanceof PaymentReceivedEvent) {
    fulfilOrder(event.getWallet().getStoreExternalId(), event.getTransaction());
} else if (event instanceof PaymentNotConfirmedEvent) {
    markPending(event.getWallet().getStoreExternalId());
} else if (event instanceof WithdrawalEvent) {
    recordPayout(((WithdrawalEvent) event).getWithdrawalId());
}
```

The three event types share the `WebhookEvent` accessors, so the fields common to all of them can be
read without casting. `PaymentNotConfirmedEvent` arrives with every member prefixed `unconfirmed_` on
the wire; the client strips that prefix, so it reads like the other two.

`constructEvent` throws `DvNetInvalidWebhookException` when the signature does not match or the
payload is not a known webhook shape. To split the two steps, use `verify(payload, signature,
secret)` and `parse(payload)`.

DV.net retries a delivery up to thirty times until your endpoint acknowledges it, so make your
handler idempotent. `txHash` together with `bcUniqKey` identifies an event uniquely:

```java
WebhookTransaction transaction = event.getTransaction();
String deduplicationKey = transaction.getTxHash() + ":" + transaction.getBcUniqKey();
```

## Error handling

Every failure is a checked `DvNetException`:

| Exception | Raised when |
| --- | --- |
| `DvNetInvalidRequestException` | The API answered 4xx. |
| `DvNetServerException` | The API answered 5xx. |
| `DvNetNetworkException` | The request never got an answer. |
| `DvNetInvalidResponseDataException` | A 2xx body could not be decoded. |
| `DvNetInvalidWebhookException` | A webhook failed verification or decoding. |
| `DvNetUndefinedHostException` | No host was configured. |
| `DvNetUndefinedApiKeyException` | No API key was configured. |

The first two carry the status code, the raw body, and the parsed error list:

```java
try {
    dvNet.withdrawals().create(options);
} catch (DvNetApiException e) {
    log.warn("DV.net refused the payout: {} {}", e.getStatusCode(), e.getErrors());
} catch (DvNetException e) {
    log.error("DV.net call failed", e);
}
```

## Per-request options

`RequestOptions` overrides the host, the API key, or adds headers for a single call, which is how
you serve several stores from one process.

```java
RequestOptions options = RequestOptions.builder()
        .apiKey(otherStoreApiKey)
        .header("X-Correlation-Id", correlationId)
        .build();

List<Currency> currencies = dvNet.currencies().list(options);
```

## Payment links

`Wallets.create` already returns a ready to use `payUrl`. `PaymentLink` builds the same URL without
creating the wallet first, for example for a link in an email:

```java
String url = PaymentLink.of("https://pay.example.com", storeId, "customer-42", "customer@example.com");
```

## Custom transport

`DvNet` accepts any `HttpTransport`, which is how you tune timeouts, plug in another HTTP stack, or
stub the API in tests:

```java
try (DvNet dvNet = new DvNet(new ApacheHttpTransport(host, apiKey, customHttpClient))) {
    // ...
}
```

## Notes on the models

The response models are immutable classes with JavaBean getters, value based `equals` and
`hashCode`, and Jackson creator constructors. They tolerate unknown members, so a field the API adds
later will not break an existing integration.

Timestamps are `OffsetDateTime`. The client registers its own Jackson time module rather than
depending on `jackson-datatype-jsr310`, because some webhook payloads carry a timestamp without an
offset; those are read as UTC.

## Development

Requires a JDK 11 or later to build (the tests use Mockito); the produced bytecode targets Java 8.

```bash
mvn test
```
