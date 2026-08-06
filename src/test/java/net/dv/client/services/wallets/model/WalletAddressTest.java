package net.dv.client.services.wallets.model;

import net.dv.client.core.mapper.DvNetJsonMapper;
import net.dv.client.core.mapper.JsonMapper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The model classes all follow one template, so exercising a representative one covers the value
 * semantics and the snake case wire names of the whole package.
 */
class WalletAddressTest {

    private static final OffsetDateTime ISSUED_AT = OffsetDateTime.parse("2025-03-17T12:57:52.449795Z");

    private static WalletAddress address(final String id) {
        return new WalletAddress(id, "wallet-1", "user-1", "USDT.Tron", "tron",
                "TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV", false, ISSUED_AT, ISSUED_AT, null);
    }

    @Test
    void twoAddressesWithTheSameMembersAreEqual() {
        assertEquals(address("a-1"), address("a-1"));
        assertEquals(address("a-1").hashCode(), address("a-1").hashCode());
    }

    @Test
    void addressesDifferingInAnyMemberAreNotEqual() {
        assertNotEquals(address("a-1"), address("a-2"));
        assertNotEquals(address("a-1"), null);
        assertNotEquals(address("a-1"), "a-1");
    }

    @Test
    void toStringNamesTheClassAndItsMembers() {
        String text = address("a-1").toString();

        assertTrue(text.startsWith("WalletAddress{"));
        assertTrue(text.contains("id=a-1"));
        assertTrue(text.contains("currencyId=USDT.Tron"));
        assertTrue(text.endsWith("}"));
    }

    /**
     * The constructor parameters and the fields are both annotated, so a model read from the API can
     * be written back out without the property names drifting to camel case.
     */
    @Test
    void serializesBackToTheSnakeCaseNamesTheApiUses() throws Exception {
        JsonMapper mapper = new DvNetJsonMapper();

        String json = mapper.writeValue(address("a-1"));

        assertTrue(json.contains("\"wallet_id\":\"wallet-1\""));
        assertTrue(json.contains("\"currency_id\":\"USDT.Tron\""));
        assertTrue(json.contains("\"created_at\":\"2025-03-17T12:57:52.449795Z\""));
        assertEquals(address("a-1"), mapper.readData("{\"data\":" + json + "}", WalletAddress.class));
    }
}
