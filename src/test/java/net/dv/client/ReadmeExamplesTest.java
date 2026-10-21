package net.dv.client;

import net.dv.client.core.exception.DvNetApiException;
import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.helper.PaymentLink;
import net.dv.client.core.net.RequestOptions;
import net.dv.client.services.balances.model.ExchangeBalances;
import net.dv.client.services.balances.model.ProcessingWalletBalance;
import net.dv.client.services.currencies.model.Currency;
import net.dv.client.services.currencies.model.CurrencyRate;
import net.dv.client.services.currencies.model.ExtendedCurrencies;
import net.dv.client.services.transactions.model.UnconfirmedTransaction;
import net.dv.client.services.wallets.model.CreateWalletOptions;
import net.dv.client.services.wallets.model.Wallet;
import net.dv.client.services.wallets.model.WalletAddress;
import net.dv.client.services.wallets.model.WalletSummary;
import net.dv.client.services.webhooks.Webhooks;
import net.dv.client.services.webhooks.model.AmlCheck;
import net.dv.client.services.webhooks.model.PaymentAmlBlockedEvent;
import net.dv.client.services.webhooks.model.PaymentNotConfirmedEvent;
import net.dv.client.services.webhooks.model.PaymentReceivedEvent;
import net.dv.client.services.webhooks.model.WebhookEvent;
import net.dv.client.services.webhooks.model.WebhookTransaction;
import net.dv.client.services.webhooks.model.WithdrawalEvent;
import net.dv.client.services.withdrawals.model.CreateWithdrawalOptions;
import net.dv.client.services.withdrawals.model.Withdrawal;
import net.dv.client.services.withdrawals.model.WithdrawalStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Compiles the snippets of the README against the real API, so a rename cannot silently leave the
 * documentation pointing at methods that no longer exist. The bodies never run.
 */
class ReadmeExamplesTest {

    @Test
    void theReadmeSnippetsStillCompile() {
        assertNotNull(ReadmeExamplesTest.class);
    }

    @SuppressWarnings("unused")
    private void gettingStarted(final String storeApiKey) throws DvNetException {
        try (DvNet dvNet = new DvNet("https://merchant.example.com", storeApiKey)) {
            Wallet wallet = dvNet.wallets().create(
                    CreateWalletOptions.builder("customer-42")
                            .email("customer@example.com")
                            .amount("10.50")
                            .currency("USD")
                            .build());

            String payUrl = wallet.getPayUrl();
        }
    }

    @SuppressWarnings("unused")
    private void services(final DvNet dvNet) throws DvNetException {
        List<Currency> currencies = dvNet.currencies().list();
        ExtendedCurrencies grouped = dvNet.currencies().listExtended();
        List<CurrencyRate> rates = dvNet.currencies().rates();

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

        List<WalletSummary> balances = dvNet.wallets().hotBalances("100");

        dvNet.wallets().markDirty("TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV");

        ExchangeBalances exchange = dvNet.balances().exchange();

        List<ProcessingWalletBalance> processing = dvNet.balances().processingWallets(
                Arrays.asList("tron", "ethereum"),
                Collections.singletonList("USDT.Tron"));

        Withdrawal withdrawal = dvNet.withdrawals().create(
                CreateWithdrawalOptions.builder("TJRabPrwbZy45sbavfcjinPJC18kjpRTv8", "USDT.Tron", "25.00", "payout-77")
                        .build());

        WithdrawalStatus status = dvNet.withdrawals().get(withdrawal.getId());
        WithdrawalStatus sameStatus = dvNet.withdrawals().get("payout-77");

        dvNet.withdrawals().remove(withdrawal.getId());

        List<UnconfirmedTransaction> pending = dvNet.transactions().listUnconfirmed();
    }

    @SuppressWarnings("unused")
    private void webhook(final DvNet dvNet, final String body, final String signature, final String secret)
            throws DvNetException {
        WebhookEvent event = dvNet.webhooks().constructEvent(body, signature, secret);

        if (event instanceof PaymentReceivedEvent) {
            fulfilOrder(event.getWallet().getStoreExternalId(), event.getTransaction());
        } else if (event instanceof PaymentNotConfirmedEvent) {
            markPending(event.getWallet().getStoreExternalId());
        } else if (event instanceof PaymentAmlBlockedEvent) {
            flagForReview(((PaymentAmlBlockedEvent) event).getAmlCheck());
        } else if (event instanceof WithdrawalEvent) {
            recordPayout(((WithdrawalEvent) event).getWithdrawalId());
        }

        String deduplicationKey = event.getType() + ":" + event.getTransaction().getTxId();

        String header = Webhooks.SIGNATURE_HEADER;
        String acknowledgement = Webhooks.SUCCESS_RESPONSE;
    }

    private void flagForReview(final AmlCheck amlCheck) {
    }

    @SuppressWarnings("unused")
    private void errorHandling(final DvNet dvNet) {
        try {
            dvNet.transactions().listUnconfirmed();
        } catch (DvNetApiException e) {
            System.out.println(e.getStatusCode() + " " + e.getErrors());
        } catch (DvNetException e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    private void requestOptions(final DvNet dvNet, final String otherStoreApiKey, final String correlationId)
            throws DvNetException {
        RequestOptions options = RequestOptions.builder()
                .apiKey(otherStoreApiKey)
                .header("X-Correlation-Id", correlationId)
                .build();

        List<Currency> currencies = dvNet.currencies().list(options);
    }

    @SuppressWarnings("unused")
    private void paymentLink(final String storeId) {
        String url = PaymentLink.of("https://pay.example.com", storeId, "customer-42", "customer@example.com");
    }

    private void fulfilOrder(final String storeExternalId, final WebhookTransaction transaction) {
    }

    private void markPending(final String storeExternalId) {
    }

    private void recordPayout(final String withdrawalId) {
    }
}
