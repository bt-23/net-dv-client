package net.dv.client.services.withdrawals;

import net.dv.client.core.exception.DvNetInvalidRequestException;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpResponse;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.services.util.Payloads;
import net.dv.client.services.withdrawals.model.CreateWithdrawalOptions;
import net.dv.client.services.withdrawals.model.Withdrawal;
import net.dv.client.services.withdrawals.model.WithdrawalStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WithdrawalsTest {

    private static final String WITHDRAWAL_ID = "408a97b1-d1e3-423e-9c8b-5ae4cd902f7f";

    private HttpTransport transport;
    private Withdrawals withdrawals;

    @BeforeEach
    void setUp() {
        transport = mock(HttpTransport.class);
        withdrawals = new Withdrawals(transport);
    }

    @Test
    void createPostsEveryRequiredParameter() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.WITHDRAWAL));

        withdrawals.create(CreateWithdrawalOptions
                .builder("TWd4WrZ9wn84f5x1hZhL4DHvk738ns5jwb", "USDT.Tron", "100", "payout-1")
                .build());

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals("/api/v1/external/withdrawal-from-processing", request.getPath());
        assertTrue(request.getBody().contains("\"address_to\":\"TWd4WrZ9wn84f5x1hZhL4DHvk738ns5jwb\""));
        assertTrue(request.getBody().contains("\"currency_id\":\"USDT.Tron\""));
        assertTrue(request.getBody().contains("\"amount\":\"100\""));
        assertTrue(request.getBody().contains("\"request_id\":\"payout-1\""));
    }

    @Test
    void createDecodesTheAcceptedWithdrawal() throws Exception {
        when(transport.execute(any(HttpRequest.class))).thenReturn(new HttpResponse(200, Payloads.WITHDRAWAL));

        Withdrawal withdrawal = withdrawals.create(CreateWithdrawalOptions
                .builder("TWd4WrZ9wn84f5x1hZhL4DHvk738ns5jwb", "USDT.Tron", "100", "payout-1")
                .build());

        assertEquals(WITHDRAWAL_ID, withdrawal.getId());
        assertNull(withdrawal.getTransferId());
        assertEquals("USDT.Tron", withdrawal.getCurrencyId());
        assertEquals("100", withdrawal.getAmount());
        assertEquals("100.02", withdrawal.getAmountUsd());
        assertEquals(OffsetDateTime.parse("2025-09-23T12:27:08.166963191Z"), withdrawal.getCreatedAt());
    }

    @Test
    void createRejectsMissingRequiredParameters() {
        assertThrows(NullPointerException.class,
                () -> CreateWithdrawalOptions.builder(null, "USDT.Tron", "100", "payout-1"));
        assertThrows(NullPointerException.class,
                () -> CreateWithdrawalOptions.builder("address", null, "100", "payout-1"));
        assertThrows(NullPointerException.class,
                () -> CreateWithdrawalOptions.builder("address", "USDT.Tron", null, "payout-1"));
        assertThrows(NullPointerException.class,
                () -> CreateWithdrawalOptions.builder("address", "USDT.Tron", "100", null));
    }

    @Test
    void createPropagatesAConflictWhenTheRequestIdWasAlreadyUsed() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenThrow(new DvNetInvalidRequestException(409,
                        Payloads.error(409, "request_id", "withdrawal already exists")));

        DvNetInvalidRequestException exception = assertThrows(DvNetInvalidRequestException.class,
                () -> withdrawals.create(CreateWithdrawalOptions
                        .builder("address", "USDT.Tron", "100", "payout-1")
                        .build()));

        assertEquals(409, exception.getStatusCode());
        assertEquals("withdrawal already exists", exception.getErrors().get(0).getMessage());
    }

    @Test
    void getBuildsThePathFromTheWithdrawalId() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.WITHDRAWAL_STATUS));

        WithdrawalStatus status = withdrawals.get(WITHDRAWAL_ID);

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/api/v1/external/withdrawal-from-processing/" + WITHDRAWAL_ID, request.getPath());

        assertEquals("0f8b2c8e5d5f4a1e", status.getTxHash());
        assertEquals("completed", status.getTransfer().getStatus());
        assertEquals("sending", status.getTransfer().getStage());
        assertEquals("transfer", status.getTransfer().getKind());
    }

    @Test
    void getRejectsAMissingWithdrawalId() {
        assertThrows(NullPointerException.class, () -> withdrawals.get(null));
    }

    @Test
    void removeDeletesTheWithdrawal() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.envelope("\"ok\"")));

        withdrawals.remove(WITHDRAWAL_ID);

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.DELETE, request.getMethod());
        assertEquals("/api/v1/external/withdrawal-from-processing/" + WITHDRAWAL_ID, request.getPath());
        assertNull(request.getBody());
    }

    @Test
    void removePropagatesTheApiRefusal() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenThrow(new DvNetInvalidRequestException(422,
                        Payloads.error(422, "id", "withdrawal already sent")));

        assertThrows(DvNetInvalidRequestException.class, () -> withdrawals.remove(WITHDRAWAL_ID));
    }

    private HttpRequest capturedRequest() throws Exception {
        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        org.mockito.Mockito.verify(transport).execute(captor.capture());
        return captor.getValue();
    }
}
