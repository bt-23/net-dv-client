package net.dv.client.services.balances;

import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.mapper.JsonMapper;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.core.net.RequestOptions;
import net.dv.client.core.service.BaseService;
import net.dv.client.services.balances.model.ExchangeBalances;
import net.dv.client.services.balances.model.ProcessingWalletBalance;

import java.util.Collection;
import java.util.List;

/**
 * The funds held by the merchant: on the connected exchanges and on the processing wallets.
 */
public final class Balances extends BaseService {

    private static final String EXCHANGE_BALANCES_PATH = "/api/v1/external/exchange-balances";
    private static final String PROCESSING_WALLET_BALANCES_PATH = "/api/v1/external/processing-wallet-balances";

    /**
     * Constructs the service.
     *
     * @param transport The transport executing the requests.
     */
    public Balances(final HttpTransport transport) {
        super(transport);
    }

    /**
     * Constructs the service with an explicit mapper.
     *
     * @param transport  The transport executing the requests.
     * @param jsonMapper The mapper decoding the responses.
     */
    public Balances(final HttpTransport transport, final JsonMapper jsonMapper) {
        super(transport, jsonMapper);
    }

    /**
     * Retrieves the balances held on the exchanges connected to the merchant.
     *
     * @return The exchange balances, with their USD total.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public ExchangeBalances exchange() throws DvNetException {
        return exchange(null);
    }

    /**
     * Retrieves the balances held on the exchanges connected to the merchant.
     *
     * @param options The per request overrides, may be {@code null}.
     * @return The exchange balances, with their USD total.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public ExchangeBalances exchange(final RequestOptions options) throws DvNetException {
        HttpRequest request = HttpRequest.builder(HttpMethod.GET, EXCHANGE_BALANCES_PATH)
                .options(options)
                .build();

        return send(request, ExchangeBalances.class);
    }

    /**
     * Lists the balances of every processing wallet of the merchant.
     *
     * @return The processing wallet balances.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<ProcessingWalletBalance> processingWallets() throws DvNetException {
        return processingWallets(null, null, null);
    }

    /**
     * Lists the balances of the processing wallets matching the given filters.
     *
     * @param blockchains The blockchains to report, {@code null} or empty for all of them.
     * @param currencies  The currency identifiers to report, {@code null} or empty for all of them.
     * @return The processing wallet balances.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<ProcessingWalletBalance> processingWallets(final Collection<String> blockchains,
                                                           final Collection<String> currencies)
            throws DvNetException {
        return processingWallets(blockchains, currencies, null);
    }

    /**
     * Lists the balances of the processing wallets matching the given filters.
     *
     * @param blockchains The blockchains to report, {@code null} or empty for all of them.
     * @param currencies  The currency identifiers to report, {@code null} or empty for all of them.
     * @param options     The per request overrides, may be {@code null}.
     * @return The processing wallet balances.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<ProcessingWalletBalance> processingWallets(final Collection<String> blockchains,
                                                           final Collection<String> currencies,
                                                           final RequestOptions options) throws DvNetException {
        HttpRequest request = HttpRequest.builder(HttpMethod.GET, PROCESSING_WALLET_BALANCES_PATH)
                .query("blockchains", join(blockchains))
                .query("currencies", join(currencies))
                .options(options)
                .build();

        return sendForList(request, ProcessingWalletBalance.class);
    }

    private static String join(final Collection<String> values) {
        return values == null || values.isEmpty() ? null : String.join(",", values);
    }
}
