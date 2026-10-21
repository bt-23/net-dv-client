package net.dv.client.services.wallets;

import net.dv.client.core.exception.DvNetInvalidRequestException;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpResponse;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.services.util.Payloads;
import net.dv.client.services.wallets.model.CreateWalletOptions;
import net.dv.client.services.wallets.model.Wallet;
import net.dv.client.services.wallets.model.WalletAddress;
import net.dv.client.services.wallets.model.WalletSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WalletsTest {

    private HttpTransport transport;
    private Wallets wallets;

    @BeforeEach
    void setUp() {
        transport = mock(HttpTransport.class);
        wallets = new Wallets(transport);
    }

    @Test
    void createPostsTheCustomerIdentifier() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.WALLET));

        wallets.create(CreateWalletOptions.builder("customer-42").build());

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals("/api/v1/external/wallet", request.getPath());
        assertTrue(request.getBody().contains("\"store_external_id\":\"customer-42\""));
    }

    @Test
    void createOmitsTheOptionalParametersThatWereNotSet() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.WALLET));

        wallets.create(CreateWalletOptions.builder("customer-42").build());

        String body = capturedRequest().getBody();
        assertFalse(body.contains("email"));
        assertFalse(body.contains("amount"));
        assertFalse(body.contains("currency"));
        assertFalse(body.contains("locale"));
    }

    @Test
    void createSendsEveryOptionalParameterThatWasSet() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.WALLET));

        wallets.create(CreateWalletOptions.builder("customer-42")
                .email("customer@example.com")
                .ip("203.0.113.7")
                .amount("10.50")
                .currency("USD")
                .locale("en")
                .build());

        String body = capturedRequest().getBody();
        assertTrue(body.contains("\"email\":\"customer@example.com\""));
        assertTrue(body.contains("\"ip\":\"203.0.113.7\""));
        assertTrue(body.contains("\"amount\":\"10.50\""));
        assertTrue(body.contains("\"currency\":\"USD\""));
        assertTrue(body.contains("\"locale\":\"en\""));
    }

    @Test
    void createDecodesTheWalletAndItsAddresses() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.WALLET));

        Wallet wallet = wallets.create(CreateWalletOptions.builder("customer-42").build());

        assertEquals("9a07b545-f99e-4f51-bf37-a84fcbe4df4d", wallet.getId());
        assertEquals("customer-42", wallet.getStoreExternalId());
        assertEquals("https://pay.dv.net/0f4d1d2a/customer-42", wallet.getPayUrl());
        assertEquals("10.50", wallet.getAmountUsd());
        assertEquals("1.0002", wallet.getRates().get("USDT.Tron"));
        assertEquals(OffsetDateTime.parse("2025-03-17T12:57:52.449795Z"), wallet.getCreatedAt());

        WalletAddress address = wallet.getAddresses().get(0);
        assertEquals("TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV", address.getAddress());
        assertEquals("USDT.Tron", address.getCurrencyId());
        assertFalse(address.getDirty());
        assertNull(address.getDeletedAt());
    }

    @Test
    void createRejectsMissingOptions() {
        assertThrows(NullPointerException.class, () -> wallets.create(null));
    }

    @Test
    void createRejectsAMissingCustomerIdentifier() {
        assertThrows(NullPointerException.class, () -> CreateWalletOptions.builder(null));
    }

    @Test
    void createPropagatesApiErrors() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenThrow(new DvNetInvalidRequestException(422,
                        Payloads.error(422, "store_external_id", "must be a valid uuid")));

        DvNetInvalidRequestException exception = assertThrows(DvNetInvalidRequestException.class,
                () -> wallets.create(CreateWalletOptions.builder("not-a-uuid").build()));

        assertEquals(422, exception.getStatusCode());
        assertEquals("store_external_id", exception.getErrors().get(0).getField());
    }

    @Test
    void hotBalancesSendsNoFilterByDefault() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.HOT_WALLET_BALANCES));

        wallets.hotBalances();

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/api/v1/external/wallet/balance/hot", request.getPath());
        assertTrue(request.getQueryParameters().isEmpty());
    }

    @Test
    void hotBalancesSendsTheMinimumBalanceFilter() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.HOT_WALLET_BALANCES));

        wallets.hotBalances("10");

        assertEquals("10", capturedRequest().getQueryParameter("min_balance"));
    }

    @Test
    void markDirtyPostsTheAddress() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, "{\"code\":200,\"message\":\"success mark dirty\"}"));

        wallets.markDirty("TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV");

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals("/api/v1/external/wallet/addresses/dirty", request.getPath());
        assertEquals("{\"address\":\"TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV\"}", request.getBody());
    }

    @Test
    void markDirtyRejectsAMissingAddress() {
        assertThrows(NullPointerException.class, () -> wallets.markDirty(null));
    }

    @Test
    void hotBalancesDecodesTheAggregatedTotals() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.HOT_WALLET_BALANCES));

        List<WalletSummary> balances = wallets.hotBalances();

        assertEquals(1, balances.size());
        WalletSummary summary = balances.get(0);
        assertEquals("1520.4", summary.getBalance());
        assertEquals("1520.7", summary.getBalanceUsd());
        assertEquals(12, summary.getCount());
        assertEquals(3, summary.getCountWithBalance());
        assertEquals("USDT.Tron", summary.getCurrency().getId());
        assertEquals(1, summary.getCurrency().getSortOrder());
    }

    private HttpRequest capturedRequest() throws Exception {
        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        org.mockito.Mockito.verify(transport).execute(captor.capture());
        return captor.getValue();
    }
}
