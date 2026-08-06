package net.dv.client.core.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentLinkTest {

    private static final String HOST = "https://pay.example.com";
    private static final String STORE_ID = "0f4d1d2a-0b2f-4a24-9c02-1a3c2c1d4e5f";

    @Test
    void buildsThePathFromTheStoreAndCustomer() {
        assertEquals(HOST + "/" + STORE_ID + "/customer-42",
                PaymentLink.of(HOST, STORE_ID, "customer-42"));
    }

    @Test
    void appendsTheEmailAsAQueryParameter() {
        assertEquals(HOST + "/" + STORE_ID + "/customer-42?email=customer%40example.com",
                PaymentLink.of(HOST, STORE_ID, "customer-42", "customer@example.com"));
    }

    @Test
    void ignoresABlankEmail() {
        assertEquals(HOST + "/" + STORE_ID + "/customer-42",
                PaymentLink.of(HOST, STORE_ID, "customer-42", "  "));
    }

    @Test
    void doesNotDoubleTheSlashWhenTheHostEndsWithOne() {
        assertEquals(HOST + "/" + STORE_ID + "/customer-42",
                PaymentLink.of(HOST + "/", STORE_ID, "customer-42"));
    }

    @Test
    void encodesACustomerIdentifierThatNeedsIt() {
        assertEquals(HOST + "/" + STORE_ID + "/order+42",
                PaymentLink.of(HOST, STORE_ID, "order 42"));
    }

    @Test
    void rejectsMissingArguments() {
        assertThrows(NullPointerException.class, () -> PaymentLink.of(null, STORE_ID, "customer-42"));
        assertThrows(NullPointerException.class, () -> PaymentLink.of(HOST, null, "customer-42"));
        assertThrows(NullPointerException.class, () -> PaymentLink.of(HOST, STORE_ID, null));
    }
}
