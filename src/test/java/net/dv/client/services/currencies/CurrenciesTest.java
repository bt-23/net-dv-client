package net.dv.client.services.currencies;

import net.dv.client.core.exception.DvNetInvalidRequestException;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpResponse;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.core.net.RequestOptions;
import net.dv.client.services.currencies.model.Currency;
import net.dv.client.services.currencies.model.CurrencyRate;
import net.dv.client.services.currencies.model.ExtendedCurrencies;
import net.dv.client.services.util.Payloads;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrenciesTest {

    private HttpTransport transport;
    private Currencies currencies;

    @BeforeEach
    void setUp() {
        transport = mock(HttpTransport.class);
        currencies = new Currencies(transport);
    }

    @Test
    void listCallsTheStoreCurrenciesEndpoint() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.CURRENCIES));

        currencies.list();

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/api/v1/external/store/currencies", request.getPath());
        assertNull(request.getBody());
        assertTrue(request.getQueryParameters().isEmpty());
    }

    @Test
    void listDecodesEveryCurrency() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.CURRENCIES));

        List<Currency> result = currencies.list();

        assertEquals(2, result.size());

        Currency usdt = result.get(0);
        assertEquals("USDT.Tron", usdt.getId());
        assertEquals("USDT", usdt.getCode());
        assertEquals(6, usdt.getPrecision());
        assertFalse(usdt.getIsFiat());
        assertTrue(usdt.getHasBalance());
        assertEquals(19, usdt.getMinConfirmation());
        assertEquals("1.5", usdt.getWithdrawalMinBalance());
        assertEquals("https://cdn.dv.net/usdt.svg", usdt.getIcon().getIconSvg());
        assertEquals("https://cdn.dv.net/tron_128.png", usdt.getBlockchainIcon().getIcon128());
    }

    @Test
    void listToleratesMissingIcons() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.CURRENCIES));

        Currency bitcoin = currencies.list().get(1);

        assertEquals("BTC.Bitcoin", bitcoin.getId());
        assertNull(bitcoin.getIcon());
        assertNull(bitcoin.getBlockchainIcon());
    }

    @Test
    void listPassesTheRequestOptionsThrough() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.CURRENCIES));
        RequestOptions options = RequestOptions.builder().apiKey("other-store-key").build();

        currencies.list(options);

        assertEquals("other-store-key", capturedRequest().getOptions().getApiKey());
    }

    @Test
    void listPropagatesApiErrors() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenThrow(new DvNetInvalidRequestException(401, Payloads.error(401, "api_key", "Unauthorized")));

        DvNetInvalidRequestException exception =
                assertThrows(DvNetInvalidRequestException.class, () -> currencies.list());

        assertEquals(401, exception.getStatusCode());
        assertEquals("Unauthorized", exception.getErrors().get(0).getMessage());
    }

    @Test
    void listExtendedGroupsCurrenciesByTokenAndBlockchain() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.EXTENDED_CURRENCIES));

        ExtendedCurrencies result = currencies.listExtended();

        assertEquals("/api/v1/external/store/currencies-extended", capturedRequest().getPath());
        assertEquals(1, result.getTokens().size());
        assertEquals("USDT", result.getTokens().get(0).getName());
        assertEquals(Arrays.asList("tron", "ethereum"), result.getTokens().get(0).getBlockchains());
        assertEquals("tron", result.getBlockchains().get(0).getName());
        assertEquals(Collections.singletonList("USDT"), result.getBlockchains().get(0).getTokens());
        assertEquals("tk512", result.getCurrencies().get(0).getTokenIcon().getIcon512());
    }

    @Test
    void rateBuildsThePathFromTheCurrencyId() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.CURRENCY_RATE));

        CurrencyRate rate = currencies.rate("USDT.Tron");

        assertEquals("/api/v1/external/store/currencies/USDT.Tron/rate", capturedRequest().getPath());
        assertEquals("USDT", rate.getCode());
        assertEquals("1.0002", rate.getRate());
        assertEquals("binance", rate.getRateSource());
    }

    @Test
    void rateRejectsAMissingCurrencyId() {
        assertThrows(NullPointerException.class, () -> currencies.rate(null));
    }

    private HttpRequest capturedRequest() throws Exception {
        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        org.mockito.Mockito.verify(transport).execute(captor.capture());
        return captor.getValue();
    }
}
