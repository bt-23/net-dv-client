package net.dv.client.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DvNetApiExceptionTest {

    @Test
    void decodesTheErrorsArrayOfTheApiEnvelope() {
        String body = "{\"code\":422,\"errors\":[{\"field\":\"amount\",\"message\":\"must be positive\"},"
                + "{\"field\":\"currency_id\",\"message\":\"unknown currency\"}]}";

        DvNetApiException exception = new DvNetApiException(422, body);

        assertEquals(422, exception.getStatusCode());
        assertEquals(2, exception.getErrors().size());
        assertEquals("amount", exception.getErrors().get(0).getField());
        assertEquals("must be positive", exception.getErrors().get(0).getMessage());
        assertTrue(exception.getMessage().contains("amount: must be positive"));
        assertTrue(exception.getMessage().contains("currency_id: unknown currency"));
    }

    @Test
    void fallsBackToTheMessageMemberWhenThereIsNoErrorsArray() {
        DvNetApiException exception = new DvNetApiException(404, "{\"code\":404,\"message\":\"not found\"}");

        assertEquals(1, exception.getErrors().size());
        assertNull(exception.getErrors().get(0).getField());
        assertEquals("not found", exception.getErrors().get(0).getMessage());
    }

    @Test
    void keepsTheRawBodyWhenItIsNotTheDocumentedEnvelope() {
        DvNetApiException exception = new DvNetApiException(502, "<html>Bad Gateway</html>");

        assertTrue(exception.getErrors().isEmpty());
        assertEquals("<html>Bad Gateway</html>", exception.getResponseBody());
        assertTrue(exception.getMessage().contains("<html>Bad Gateway</html>"));
    }

    @Test
    void reportsAnEmptyBodyExplicitly() {
        DvNetApiException exception = new DvNetApiException(500, null);

        assertTrue(exception.getMessage().contains("no response body"));
        assertTrue(exception.getErrors().isEmpty());
    }

    @Test
    void everyFailureSharesTheSameBaseType() {
        assertTrue(new DvNetInvalidRequestException(400, "{}") instanceof DvNetException);
        assertTrue(new DvNetServerException(500, "{}") instanceof DvNetException);
        assertTrue(new DvNetNetworkException("boom", new RuntimeException()) instanceof DvNetException);
        assertTrue(new DvNetInvalidResponseDataException("boom") instanceof DvNetException);
        assertTrue(new DvNetInvalidWebhookException("boom") instanceof DvNetException);
        assertTrue(new DvNetUndefinedHostException("boom") instanceof DvNetException);
        assertTrue(new DvNetUndefinedApiKeyException("boom") instanceof DvNetException);
    }
}
