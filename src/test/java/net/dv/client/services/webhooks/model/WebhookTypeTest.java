package net.dv.client.services.webhooks.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebhookTypeTest {

    @Test
    void fromValueResolvesEveryDocumentedType() {
        assertEquals(WebhookType.PAYMENT_RECEIVED, WebhookType.fromValue("PaymentReceived"));
        assertEquals(WebhookType.PAYMENT_NOT_CONFIRMED, WebhookType.fromValue("PaymentNotConfirmed"));
        assertEquals(WebhookType.WITHDRAWAL_FROM_PROCESSING_RECEIVED,
                WebhookType.fromValue("WithdrawalFromProcessingReceived"));
    }

    @Test
    void fromValueFallsBackToUnknownSoNewTypesDoNotBreakIntegrations() {
        assertEquals(WebhookType.UNKNOWN, WebhookType.fromValue("SomethingIntroducedLater"));
        assertEquals(WebhookType.UNKNOWN, WebhookType.fromValue(null));
    }

    @Test
    void getValueReturnsTheWireRepresentation() {
        assertEquals("PaymentReceived", WebhookType.PAYMENT_RECEIVED.getValue());
        assertEquals("", WebhookType.UNKNOWN.getValue());
    }
}
