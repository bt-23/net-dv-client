package net.dv.client.services.transactions;

import net.dv.client.core.exception.DvNetInvalidRequestException;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpResponse;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.services.transactions.model.UnconfirmedTransaction;
import net.dv.client.services.util.Payloads;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionsTest {

    private HttpTransport transport;
    private Transactions transactions;

    @BeforeEach
    void setUp() {
        transport = mock(HttpTransport.class);
        transactions = new Transactions(transport);
    }

    @Test
    void listUnconfirmedCallsTheTransferEndpoint() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.UNCONFIRMED_TRANSACTIONS));

        transactions.listUnconfirmed();

        HttpRequest request = capturedRequest();
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/api/v1/external/transactions/unconfirmed/transfer", request.getPath());
        assertTrue(request.getQueryParameters().isEmpty());
    }

    @Test
    void listUnconfirmedDecodesTheTransfer() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.UNCONFIRMED_TRANSACTIONS));

        List<UnconfirmedTransaction> result = transactions.listUnconfirmed();

        assertEquals(1, result.size());
        UnconfirmedTransaction transaction = result.get(0);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", transaction.getId());
        assertEquals("2be41b0cad76bc5699c3da5d5a1d390f", transaction.getTxHash());
        assertEquals("0", transaction.getBcUniqKey());
        assertEquals("LTC.Litecoin", transaction.getCurrencyId());
        assertEquals("ltc1qsender", transaction.getFromAddress());
        assertEquals("ltc1qreceiver", transaction.getToAddress());
        assertEquals("0.02552778", transaction.getAmount());
        assertEquals("2.395", transaction.getAmountUsd());
        assertEquals(OffsetDateTime.parse("2025-11-07T06:10:49.104Z"), transaction.getNetworkCreatedAt());
    }

    @Test
    void listUnconfirmedReturnsAnEmptyListWhenThereIsNothingPending() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.envelope("[]")));

        assertTrue(transactions.listUnconfirmed().isEmpty());
    }

    @Test
    void listUnconfirmedTreatsANullPayloadAsEmpty() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenReturn(new HttpResponse(200, Payloads.envelope("null")));

        assertTrue(transactions.listUnconfirmed().isEmpty());
    }

    @Test
    void listUnconfirmedPropagatesApiErrors() throws Exception {
        when(transport.execute(any(HttpRequest.class)))
                .thenThrow(new DvNetInvalidRequestException(401, Payloads.error(401, "api_key", "Unauthorized")));

        assertThrows(DvNetInvalidRequestException.class, () -> transactions.listUnconfirmed());
    }

    private HttpRequest capturedRequest() throws Exception {
        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        org.mockito.Mockito.verify(transport).execute(captor.capture());
        return captor.getValue();
    }
}
