package net.dv.client.services.currencies;

import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.mapper.JsonMapper;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.core.net.RequestOptions;
import net.dv.client.core.service.BaseService;
import net.dv.client.services.currencies.model.Currency;
import net.dv.client.services.currencies.model.CurrencyRate;
import net.dv.client.services.currencies.model.ExtendedCurrencies;

import java.util.List;
import java.util.Objects;

/**
 * The currency catalogue of a store, and the exchange rates used to price an invoice.
 */
public final class Currencies extends BaseService {

    private static final String CURRENCIES_PATH = "/api/v1/external/store/currencies";
    private static final String EXTENDED_CURRENCIES_PATH = "/api/v1/external/store/currencies-extended";

    /**
     * Constructs the service.
     *
     * @param transport The transport executing the requests.
     */
    public Currencies(final HttpTransport transport) {
        super(transport);
    }

    /**
     * Constructs the service with an explicit mapper.
     *
     * @param transport  The transport executing the requests.
     * @param jsonMapper The mapper decoding the responses.
     */
    public Currencies(final HttpTransport transport, final JsonMapper jsonMapper) {
        super(transport, jsonMapper);
    }

    /**
     * Lists the currencies enabled on the store.
     *
     * @return The currencies enabled on the store.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<Currency> list() throws DvNetException {
        return list(null);
    }

    /**
     * Lists the currencies enabled on the store.
     *
     * @param options The per request overrides, may be {@code null}.
     * @return The currencies enabled on the store.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<Currency> list(final RequestOptions options) throws DvNetException {
        HttpRequest request = HttpRequest.builder(HttpMethod.GET, CURRENCIES_PATH)
                .options(options)
                .build();

        return sendForList(request, Currency.class);
    }

    /**
     * Lists the currencies enabled on the store, grouped by token and by blockchain.
     *
     * @return The extended catalogue.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public ExtendedCurrencies listExtended() throws DvNetException {
        return listExtended(null);
    }

    /**
     * Lists the currencies enabled on the store, grouped by token and by blockchain.
     *
     * @param options The per request overrides, may be {@code null}.
     * @return The extended catalogue.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public ExtendedCurrencies listExtended(final RequestOptions options) throws DvNetException {
        HttpRequest request = HttpRequest.builder(HttpMethod.GET, EXTENDED_CURRENCIES_PATH)
                .options(options)
                .build();

        return send(request, ExtendedCurrencies.class);
    }

    /**
     * Retrieves the exchange rate of a currency.
     *
     * @param currencyId The currency identifier, for example {@code USDT.Tron}.
     * @return The exchange rate of the currency.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public CurrencyRate rate(final String currencyId) throws DvNetException {
        return rate(currencyId, null);
    }

    /**
     * Retrieves the exchange rate of a currency.
     *
     * @param currencyId The currency identifier, for example {@code USDT.Tron}.
     * @param options    The per request overrides, may be {@code null}.
     * @return The exchange rate of the currency.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public CurrencyRate rate(final String currencyId, final RequestOptions options) throws DvNetException {
        Objects.requireNonNull(currencyId, "currencyId");

        HttpRequest request = HttpRequest.builder(HttpMethod.GET, CURRENCIES_PATH + "/" + currencyId + "/rate")
                .options(options)
                .build();

        return send(request, CurrencyRate.class);
    }
}
