package net.dv.client.services.webhooks;

import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.exception.DvNetInvalidWebhookException;
import net.dv.client.services.util.WebhookPayloads;
import net.dv.client.services.webhooks.model.PaymentNotConfirmedEvent;
import net.dv.client.services.webhooks.model.PaymentReceivedEvent;
import net.dv.client.services.webhooks.model.WebhookEvent;
import net.dv.client.services.webhooks.model.WebhookType;
import net.dv.client.services.webhooks.model.WithdrawalEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dv.client.core.helper.Strings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebhooksTest {

    private static final String SECRET = "the-store-webhook-secret";

    private Webhooks webhooks;

    @BeforeEach
    void setUp() {
        webhooks = new Webhooks();
    }

    @Test
    void signMatchesTheDocumentedSha256OfBodyAndSecret() throws Exception {
        String payload = "{\"type\":\"PaymentReceived\"}";

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String expected = Strings.toHex(digest.digest((payload + SECRET).getBytes(StandardCharsets.UTF_8)));

        assertEquals(expected, webhooks.sign(payload, SECRET));
    }

    @Test
    void verifyAcceptsASignatureProducedWithTheSameSecret() {
        String payload = WebhookPayloads.PAYMENT_RECEIVED;

        assertTrue(webhooks.verify(payload, webhooks.sign(payload, SECRET), SECRET));
    }

    @Test
    void verifyIgnoresSurroundingWhitespaceInTheHeader() {
        String payload = WebhookPayloads.PAYMENT_RECEIVED;

        assertTrue(webhooks.verify(payload, "  " + webhooks.sign(payload, SECRET) + "\n", SECRET));
    }

    @Test
    void verifyRejectsASignatureProducedWithAnotherSecret() {
        String payload = WebhookPayloads.PAYMENT_RECEIVED;

        assertFalse(webhooks.verify(payload, webhooks.sign(payload, "another-secret"), SECRET));
    }

    @Test
    void verifyRejectsATamperedPayload() {
        String signature = webhooks.sign(WebhookPayloads.PAYMENT_RECEIVED, SECRET);

        assertFalse(webhooks.verify(WebhookPayloads.PAYMENT_RECEIVED.replace("2.395", "9999"), signature, SECRET));
    }

    @Test
    void verifyRejectsMissingArguments() {
        assertFalse(webhooks.verify(null, "signature", SECRET));
        assertFalse(webhooks.verify("{}", null, SECRET));
        assertFalse(webhooks.verify("{}", "signature", null));
    }

    @Test
    void parseDecodesAConfirmedPayment() throws Exception {
        WebhookEvent event = webhooks.parse(WebhookPayloads.PAYMENT_RECEIVED);

        PaymentReceivedEvent payment = assertInstanceOf(PaymentReceivedEvent.class, event);
        assertEquals(WebhookType.PAYMENT_RECEIVED, payment.getType());
        assertEquals("PaymentReceived", payment.getRawType());
        assertEquals("completed", payment.getStatus());
        assertEquals("2.395", payment.getAmount());
        assertEquals(OffsetDateTime.parse("2025-03-17T12:57:52.449795Z"), payment.getCreatedAt());
        assertEquals(OffsetDateTime.parse("2025-03-17T12:57:19Z"), payment.getPaidAt());

        assertEquals("LTC", payment.getTransaction().getCurrency());
        assertEquals("LTC.Litecoin", payment.getTransaction().getCurrencyId());
        assertEquals("0.02552778", payment.getTransaction().getAmount());
        assertEquals("2.395", payment.getTransaction().getAmountUsd());
        assertEquals("0", payment.getTransaction().getBcUniqKey());
        assertEquals("4bbc91fd-a950-4fd0-83f3-9f1c09a6b54f", payment.getTransaction().getTxId());

        assertEquals("1", payment.getWallet().getStoreExternalId());
        assertEquals("9a07b545-f99e-4f51-bf37-a84fcbe4df4d", payment.getWallet().getId());
    }

    @Test
    void parseDecodesAMempoolPaymentDespiteThePrefixedMembers() throws Exception {
        WebhookEvent event = webhooks.parse(WebhookPayloads.PAYMENT_NOT_CONFIRMED);

        PaymentNotConfirmedEvent pending = assertInstanceOf(PaymentNotConfirmedEvent.class, event);
        assertEquals(WebhookType.PAYMENT_NOT_CONFIRMED, pending.getType());
        assertEquals("completed", pending.getStatus());
        assertEquals("1000000000000", pending.getAmount());

        assertEquals("BTC", pending.getTransaction().getCurrency());
        assertEquals("BTC.Bitcoin", pending.getTransaction().getCurrencyId());
        assertEquals("tx_hash_example", pending.getTransaction().getTxHash());
        assertEquals("bc_uniq_key_example", pending.getTransaction().getBcUniqKey());

        assertEquals("store_external_example", pending.getWallet().getStoreExternalId());
        assertEquals("5f185a18-a8d5-469d-a853-7fbcb048635b", pending.getWallet().getId());
    }

    @Test
    void parseReadsAMempoolTimestampThatCarriesNoOffsetAsUtc() throws Exception {
        PaymentNotConfirmedEvent pending =
                assertInstanceOf(PaymentNotConfirmedEvent.class, webhooks.parse(WebhookPayloads.PAYMENT_NOT_CONFIRMED));

        assertEquals(OffsetDateTime.parse("2025-10-06T12:39:39.457399475Z"), pending.getCreatedAt());
    }

    @Test
    void parseDecodesAWithdrawalFromItsType() throws Exception {
        WebhookEvent event = webhooks.parse(WebhookPayloads.WITHDRAWAL);

        WithdrawalEvent withdrawal = assertInstanceOf(WithdrawalEvent.class, event);
        assertEquals(WebhookType.WITHDRAWAL_FROM_PROCESSING_RECEIVED, withdrawal.getType());
        assertEquals("completed", withdrawal.getStatus());
        assertEquals("100", withdrawal.getAmount());
        assertEquals("408a97b1-d1e3-423e-9c8b-5ae4cd902f7f", withdrawal.getTransaction().getTxId());
        assertEquals("8a279ec7-31fb-41f2-a2a5-3d066b77bd8a", withdrawal.getWallet().getId());
        assertEquals(OffsetDateTime.parse("2025-09-23T12:27:08.166963191+03:00"), withdrawal.getPaidAt());
    }

    @Test
    void parseDecodesAWithdrawalAnnouncedThroughTheWithdrawalIdMember() throws Exception {
        WebhookEvent event = webhooks.parse(WebhookPayloads.WITHDRAWAL_WITH_ID);

        WithdrawalEvent withdrawal = assertInstanceOf(WithdrawalEvent.class, event);
        assertEquals("408a97b1-d1e3-423e-9c8b-5ae4cd902f7f", withdrawal.getWithdrawalId());
        assertEquals(WebhookType.UNKNOWN, withdrawal.getType());
        assertEquals("WithdrawalReceived", withdrawal.getRawType());
    }

    @Test
    void parseRejectsAPayloadThatIsNotJson() {
        assertThrows(DvNetInvalidWebhookException.class, () -> webhooks.parse("not json at all"));
    }

    @Test
    void parseRejectsAnEmptyPayload() {
        assertThrows(DvNetInvalidWebhookException.class, () -> webhooks.parse(""));
        assertThrows(DvNetInvalidWebhookException.class, () -> webhooks.parse(null));
    }

    @Test
    void parseRejectsAnUnknownWebhookShape() {
        DvNetInvalidWebhookException exception = assertThrows(DvNetInvalidWebhookException.class,
                () -> webhooks.parse(WebhookPayloads.UNKNOWN_SHAPE));

        assertTrue(exception.getMessage().contains("unconfirmed_type"));
    }

    @Test
    void parseRejectsAJsonArray() {
        assertThrows(DvNetInvalidWebhookException.class, () -> webhooks.parse("[]"));
    }

    @Test
    void constructEventVerifiesBeforeDecoding() throws Exception {
        String payload = WebhookPayloads.PAYMENT_RECEIVED;
        String signature = webhooks.sign(payload, SECRET);

        WebhookEvent event = webhooks.constructEvent(payload, signature, SECRET);

        assertNotNull(event);
        assertInstanceOf(PaymentReceivedEvent.class, event);
    }

    @Test
    void constructEventRefusesAnInvalidSignature() {
        DvNetException exception = assertThrows(DvNetInvalidWebhookException.class,
                () -> webhooks.constructEvent(WebhookPayloads.PAYMENT_RECEIVED, "deadbeef", SECRET));

        assertTrue(exception.getMessage().contains("signature"));
    }

    @Test
    void signatureHeaderMatchesTheOneDvNetSends() {
        assertEquals("X-Sign", Webhooks.SIGNATURE_HEADER);
    }
}
