package net.dv.client.services.balances;

import net.dv.client.core.exception.DvNetServerException;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpResponse;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.services.balances.model.EvmData;
import net.dv.client.services.balances.model.ExchangeBalances;
import net.dv.client.services.balances.model.ProcessingWalletBalance;
import net.dv.client.services.balances.model.TronData;
import net.dv.client.services.util.Payloads;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BalancesTest {

    private HttpTransport transport;
    private Balances balances;

    @BeforeEach
    void setUp() {
        transport = mock(HttpTransport.class);
        balances = new Balances(transport);
    }

    @Test
    void exchangeDecodesTheTotalAndEveryCurrency() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.EXCHANGE_BALANCES));

        ExchangeBalances result = balances.exchange();

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/api/v1/external/exchange-balances", request.getPath());

        assertEquals("3120.55", result.getTotalUsd());
        assertEquals(2, result.getBalances().size());
        assertEquals("USDT", result.getBalances().get(0).getCurrency());
        assertEquals("3000.10", result.getBalances().get(0).getAmount());
        assertEquals("119.85", result.getBalances().get(1).getAmountUsd());
    }

    @Test
    void exchangePropagatesServerErrors() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenThrow(new DvNetServerException(500, Payloads.error(500, "", "internal error")));

        DvNetServerException exception = assertThrows(DvNetServerException.class, () -> balances.exchange());

        assertEquals(500, exception.getStatusCode());
    }

    @Test
    void processingWalletsSendsNoFilterByDefault() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.PROCESSING_WALLET_BALANCES));

        balances.processingWallets();

        HttpRequest request = capturedRequest();
        assertEquals("/api/v1/external/processing-wallet-balances", request.getPath());
        assertTrue(request.getQueryParameters().isEmpty());
    }

    @Test
    void processingWalletsRepeatsTheFilterKeyForEveryValue() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.PROCESSING_WALLET_BALANCES));

        balances.processingWallets(Arrays.asList("tron", "ethereum"), Collections.singletonList("USDT.Tron"));

        HttpRequest request = capturedRequest();
        assertEquals(Arrays.asList("tron", "ethereum"), request.getQueryParameters().get("blockchains"));
        assertEquals(Collections.singletonList("USDT.Tron"), request.getQueryParameters().get("currencies"));
    }

    @Test
    void processingWalletsIgnoresEmptyFilters() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.PROCESSING_WALLET_BALANCES));

        balances.processingWallets(Collections.<String>emptyList(), null);

        assertTrue(capturedRequest().getQueryParameters().isEmpty());
    }

    @Test
    void processingWalletsDecodesTheNativeBalanceAndAssets() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.PROCESSING_WALLET_BALANCES));

        ProcessingWalletBalance wallet = balances.processingWallets().get(0);

        assertEquals("TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV", wallet.getAddress());
        assertEquals("tron", wallet.getBlockchain());
        assertEquals("TRX.Tron", wallet.getCurrency().getId());
        assertNull(wallet.getCurrency().getSortOrder());
        assertEquals("1250.5", wallet.getBalance().getNativeToken());
        assertEquals("299.6", wallet.getBalance().getNativeTokenUsd());
        assertEquals(1, wallet.getAssets().size());
        assertEquals("USDT", wallet.getAssets().get(0).getIdentity());
        assertEquals("800.25", wallet.getAssets().get(0).getAmount());
    }

    @Test
    void processingWalletsDecodesTheTronResources() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.PROCESSING_WALLET_BALANCES));

        TronData tron = balances.processingWallets().get(0).getAdditionalData().getTronData();

        assertEquals("1000", tron.getStackedTrx());
        assertEquals("65000", tron.getAvailableEnergyForUse());
        assertEquals("2000", tron.getAvailableBandwidthForUse());
        assertEquals("42", tron.getTronTransferData().getMaxTransfersTrc20());
        assertEquals("310", tron.getTronTransferData().getMaxTransfersNative());
        assertNull(balances.processingWallets().get(0).getAdditionalData().getEvmData());
    }

    @Test
    void processingWalletsDecodesTheEvmGasEstimates() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.PROCESSING_WALLET_BALANCES));

        ProcessingWalletBalance wallet = balances.processingWallets().get(1);
        EvmData evm = wallet.getAdditionalData().getEvmData();

        assertNull(wallet.getBalance());
        assertNull(wallet.getAdditionalData().getTronData());
        assertEquals("10000000", evm.getSuggestedGasPrice());
        assertEquals("120", evm.getMaxTransfersErc20());
        assertEquals("300", evm.getMaxTransfersNative());
        assertEquals("650000000000", evm.getCostPerErc20());
        assertEquals("210000000000", evm.getCostPerNative());
        assertTrue(evm.getL2());
        assertEquals("30000000000", evm.getL1DataFeeEstimate());
    }

    private HttpRequest capturedRequest() throws Exception {
        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        org.mockito.Mockito.verify(transport).execute(captor.capture());
        return captor.getValue();
    }
}
