package net.dv.client;

import net.dv.client.core.net.HttpTransport;
import net.dv.client.core.net.impl.ApacheHttpTransport;
import net.dv.client.services.balances.Balances;
import net.dv.client.services.currencies.Currencies;
import net.dv.client.services.transactions.Transactions;
import net.dv.client.services.wallets.Wallets;
import net.dv.client.services.webhooks.Webhooks;
import net.dv.client.services.withdrawals.Withdrawals;

import java.io.Closeable;
import java.util.Objects;

/**
 * The entry point of the DV.net client.
 *
 * <p>Create one instance per store and share it: it owns a pooled HTTP client, so building a new
 * one per request would leak connections. Close it when your application shuts down.</p>
 *
 * <pre>{@code
 * try (DvNet dvNet = new DvNet("https://merchant.example.com", "your_store_api_key")) {
 *     Wallet wallet = dvNet.wallets().create(
 *             CreateWalletOptions.builder("customer-42")
 *                     .email("customer@example.com")
 *                     .amount("10.50")
 *                     .currency("USD")
 *                     .build());
 *
 *     redirect(wallet.getPayUrl());
 * }
 * }</pre>
 */
public class DvNet implements Closeable {

    private static final String DEFAULT_HOST = "https://cloud.dv.net";

    private final HttpTransport transport;

    private final Currencies currencies;
    private final Wallets wallets;
    private final Balances balances;
    private final Withdrawals withdrawals;
    private final Transactions transactions;
    private final Webhooks webhooks;

    /**
     * Constructs a client for a store.
     *
     * @param apiKey The store API key, found in the project settings of your merchant.
     */
    public DvNet(final String apiKey) {
        this(DEFAULT_HOST, apiKey);
    }

    /**
     * Constructs a client for a store.
     *
     * @param host   The merchant API host, for example {@code https://merchant.example.com}.
     * @param apiKey The store API key, found in the project settings of your merchant.
     */
    public DvNet(final String host, final String apiKey) {
        this(new ApacheHttpTransport(host, apiKey));
    }

    /**
     * Constructs a client on top of a custom transport, which is how you plug in another HTTP
     * stack, tune timeouts, or stub the API in tests.
     *
     * @param transport The transport executing the requests.
     */
    public DvNet(final HttpTransport transport) {
        this.transport = Objects.requireNonNull(transport, "transport");

        this.currencies = new Currencies(transport);
        this.wallets = new Wallets(transport);
        this.balances = new Balances(transport);
        this.withdrawals = new Withdrawals(transport);
        this.transactions = new Transactions(transport);
        this.webhooks = new Webhooks();
    }

    /**
     * Returns the currency catalogue and exchange rates of the store.
     *
     * @return The currencies service.
     */
    public Currencies currencies() {
        return currencies;
    }

    /**
     * Returns the service issuing deposit addresses to your customers.
     *
     * @return The wallets service.
     */
    public Wallets wallets() {
        return wallets;
    }

    /**
     * Returns the service reading the exchange and processing wallet balances.
     *
     * @return The balances service.
     */
    public Balances balances() {
        return balances;
    }

    /**
     * Returns the service paying out from the processing wallets.
     *
     * @return The withdrawals service.
     */
    public Withdrawals withdrawals() {
        return withdrawals;
    }

    /**
     * Returns the service listing the transfers waiting for confirmations.
     *
     * @return The transactions service.
     */
    public Transactions transactions() {
        return transactions;
    }

    /**
     * Returns the service verifying and decoding incoming webhooks.
     *
     * @return The webhooks service.
     */
    public Webhooks webhooks() {
        return webhooks;
    }

    /**
     * Returns the transport shared by every service.
     *
     * @return The transport.
     */
    public HttpTransport getTransport() {
        return transport;
    }

    /**
     * Releases the connection pool held by the transport.
     */
    @Override
    public void close() {
        transport.close();
    }
}
