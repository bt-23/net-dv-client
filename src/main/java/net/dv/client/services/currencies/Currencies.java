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

/**
 * The currency catalogue of a store, and the exchange rates used to price an invoice.
 */
public final class Currencies extends BaseService {

    private static final String CURRENCIES_PATH = "/api/v1/external/store/currencies";
    private static final String EXTENDED_CURRENCIES_PATH = "/api/v1/external/store/currencies-extended";
    private static final String RATES_PATH = "/api/v1/external/store/currencies/rate";

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
     * Lists the USD exchange rates of the currencies enabled on the store, one entry per currency
     * ticker. The merchant API has no per currency rate route.
     *
     * @return The exchange rates of the store currencies.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<CurrencyRate> rates() throws DvNetException {
        return rates(null);
    }

    /**
     * Lists the USD exchange rates of the currencies enabled on the store, one entry per currency
     * ticker. The merchant API has no per currency rate route.
     *
     * @param options The per request overrides, may be {@code null}.
     * @return The exchange rates of the store currencies.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<CurrencyRate> rates(final RequestOptions options) throws DvNetException {
        HttpRequest request = HttpRequest.builder(HttpMethod.GET, RATES_PATH)
                .options(options)
                .build();

        return sendForList(request, CurrencyRate.class);
    }
}
