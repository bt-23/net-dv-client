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
List<CurrencyRate> rates = dvNet.currencies().rates();
```

`listExtended()` returns the same catalogue grouped by token and by blockchain, which is what you
want when rendering a currency picker.

`rates()` returns one USD rate per ticker (`USDT`, `BTC`, ...). The server has no per currency rate
route. `getRate()` already includes the store rate scale, and `getOriginalRate()` is the raw source
rate.

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

Flag an address that received tainted funds so it is no longer handed out:

```java
dvNet.wallets().markDirty("TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV");
```

An active address reports `getDeletedAt() == null`. On the wire the server sends Go's zero time
`0001-01-01T00:00:00Z`, which the client maps to `null`.

### Balances

```java
ExchangeBalances exchange = dvNet.balances().exchange();

List<ProcessingWalletBalance> processing = dvNet.balances().processingWallets(
        Arrays.asList("tron", "ethereum"),
        Collections.singletonList("USDT.Tron"));
```

Each filter value is sent as a repeated query key (`blockchains=tron&blockchains=ethereum`). The
server does not split comma separated values, so a joined string would match nothing.

### Withdrawals

`requestId` is your own identifier and must be unique. The server rejects a duplicate with a 422,
so it never pays out twice. It does not return the existing withdrawal, though. After a timeout,
look the withdrawal up by its `requestId`: `get` accepts either the withdrawal ID or your request
ID.

```java
Withdrawal withdrawal = dvNet.withdrawals().create(
        CreateWithdrawalOptions.builder("TJRabPrwbZy45sbavfcjinPJC18kjpRTv8", "USDT.Tron", "25.00", "payout-77")
                .build());

WithdrawalStatus status = dvNet.withdrawals().get(withdrawal.getId());
WithdrawalStatus sameStatus = dvNet.withdrawals().get("payout-77");

dvNet.withdrawals().remove(withdrawal.getId()); // only while still queued
```

| Status | Meaning when creating a withdrawal |
| --- | --- |
| 422 | Duplicate `requestId`, or a validation failure |
| 423 | Processing is not initialised for the merchant |
| 404 | No processing wallet holds the currency |
| 409 | The address does not match the currency |
| 406 | The destination is one of your hot wallets |

### Transactions

```java
List<UnconfirmedTransaction> pending = dvNet.transactions().listUnconfirmed();
```

## Webhooks

This section follows the dv-merchant server source. The published webhook guide differs from it in
several places.

DV.net signs each delivery with the SHA-256 hash of the raw body concatenated with your webhook
secret, hex encoded, and sends it in the `X-Sign` header as a `POST` with
`Content-Type: application/json`.

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
} else if (event instanceof PaymentAmlBlockedEvent) {
    flagForReview(((PaymentAmlBlockedEvent) event).getAmlCheck());
} else if (event instanceof WithdrawalEvent) {
    recordPayout(((WithdrawalEvent) event).getWithdrawalId());
}

response.setContentType("application/json");
response.getWriter().write(Webhooks.SUCCESS_RESPONSE);
```

### Acknowledging a delivery

DV.net only counts a delivery as acknowledged when the response body is `{"success":true}`. The
status code alone is not enough: an empty `200` is treated as a failure and retried. Redeliveries
back off exponentially, starting at one minute and capped at one day, for up to thirty attempts by
default. Return `Webhooks.SUCCESS_RESPONSE` once the event is stored, including for a duplicate you
already handled.

### Event shapes

| Event | `type` on the wire | Notes |
| --- | --- | --- |
| `PaymentReceivedEvent` | `PaymentReceived` | `status` is always `completed`. |
| `PaymentNotConfirmedEvent` | `PaymentNotConfirmed` | Every member, nested ones included, is prefixed `unconfirmed_`; the client strips the prefix. |
| `PaymentAmlBlockedEvent` | `PaymentAMLBlocked` | Sent to endpoints subscribed to `PaymentNotConfirmed`. Unprefixed, with an extra `aml_check`. The funds are not credited. |
| `WithdrawalEvent` | `WithdrawalFromProcessingReceived` | Carries `withdrawal_id`, but no `status` and no `wallet`, so `getStatus()` and `getWallet()` return `null`. |

Other details the guide gets wrong:

- **Timestamps never carry an offset**, for example `2025-10-06T12:39:39.457399475`. The server
  stores them in Postgres `timestamp` columns without a time zone. The client reads them as UTC.
- **`paid_at` is the blockchain time of the transaction**, and `created_at` is when DV.net recorded
  it.
- **A deposit can be unlinked from any customer wallet.** In that case `wallet.id` is `null` and
  `store_external_id` is `""`. `bc_uniq_key` may also be `null`.
- **Manual resends and dashboard test deliveries skip the signature** when the store has no secret:
  `X-Sign` is then empty. `verify` always rejects an empty signature or a blank secret, so set a
  secret in the merchant settings.

`constructEvent` throws `DvNetInvalidWebhookException` when the signature does not match or the
payload is not a known webhook shape. To split the two steps, use `verify(payload, signature,
secret)` and `parse(payload)`.

### Deduplication

Besides the automatic retries, a merchant can resend any webhook by hand from the dashboard, and a
manual resend bypasses the server's own already-sent check. Deduplicate on the event type together
with the transaction ID, which is the same key the server uses:

```java
String deduplicationKey = event.getType() + ":" + event.getTransaction().getTxId();
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
depending on `jackson-datatype-jsr310`, for two reasons. Webhook timestamps carry no offset and are
read as UTC. Go's zero time `0001-01-01T00:00:00Z` means "unset" and is mapped to `null`.

## Development

Requires a JDK 11 or later to build (the tests use Mockito); the produced bytecode targets Java 8.

```bash
mvn test
```
