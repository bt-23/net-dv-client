package net.dv.client.services.wallets;

import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.mapper.JsonMapper;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.core.net.RequestOptions;
import net.dv.client.core.service.BaseService;
import net.dv.client.services.wallets.model.CreateWalletOptions;
import net.dv.client.services.wallets.model.Wallet;
import net.dv.client.services.wallets.model.WalletSummary;

import java.util.List;
import java.util.Objects;

/**
 * Customer wallets: issuing deposit addresses and reading the aggregated hot wallet balances.
 */
public final class Wallets extends BaseService {

    private static final String WALLET_PATH = "/api/v1/external/wallet";
    private static final String HOT_BALANCE_PATH = "/api/v1/external/wallet/balance/hot";

    /**
     * Constructs the service.
     *
     * @param transport The transport executing the requests.
     */
    public Wallets(final HttpTransport transport) {
        super(transport);
    }

    /**
     * Constructs the service with an explicit mapper.
     *
     * @param transport  The transport executing the requests.
     * @param jsonMapper The mapper decoding the responses.
     */
    public Wallets(final HttpTransport transport, final JsonMapper jsonMapper) {
        super(transport, jsonMapper);
    }

    /**
     * Issues deposit addresses to a customer, or returns the ones already issued when the customer
     * is known to the store.
     *
     * @param options The wallet parameters.
     * @return The customer wallet, including the deposit addresses and the hosted payment page.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public Wallet create(final CreateWalletOptions options) throws DvNetException {
        return create(options, null);
    }

    /**
     * Issues deposit addresses to a customer, or returns the ones already issued when the customer
     * is known to the store.
     *
     * @param options        The wallet parameters.
     * @param requestOptions The per request overrides, may be {@code null}.
     * @return The customer wallet, including the deposit addresses and the hosted payment page.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public Wallet create(final CreateWalletOptions options, final RequestOptions requestOptions)
            throws DvNetException {
        Objects.requireNonNull(options, "options");

        HttpRequest request = HttpRequest.builder(HttpMethod.POST, WALLET_PATH)
                .body(jsonMapper.writeValue(options))
                .options(requestOptions)
                .build();

        return send(request, Wallet.class);
    }

    /**
     * Lists the aggregated hot wallet balances of the store, one entry per currency.
     *
     * @return The hot wallet balances.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<WalletSummary> hotBalances() throws DvNetException {
        return hotBalances(null, null);
    }

    /**
     * Lists the aggregated hot wallet balances of the store, one entry per currency.
     *
     * @param minBalance The smallest balance to report, as a decimal string. Pass {@code null} to
     *                   report every currency.
     * @return The hot wallet balances.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<WalletSummary> hotBalances(final String minBalance) throws DvNetException {
        return hotBalances(minBalance, null);
    }

    /**
     * Lists the aggregated hot wallet balances of the store, one entry per currency.
     *
     * @param minBalance The smallest balance to report, as a decimal string. Pass {@code null} to
     *                   report every currency.
     * @param options    The per request overrides, may be {@code null}.
     * @return The hot wallet balances.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<WalletSummary> hotBalances(final String minBalance, final RequestOptions options)
            throws DvNetException {
        HttpRequest request = HttpRequest.builder(HttpMethod.GET, HOT_BALANCE_PATH)
                .query("min_balance", minBalance)
                .options(options)
                .build();

        return sendForList(request, WalletSummary.class);
    }
}
