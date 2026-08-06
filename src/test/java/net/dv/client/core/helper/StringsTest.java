package net.dv.client.core.helper;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringsTest {

    @Test
    void treatsNullEmptyAndWhitespaceAsBlank() {
        assertTrue(Strings.isBlank(null));
        assertTrue(Strings.isBlank(""));
        assertTrue(Strings.isBlank(" "));
        assertTrue(Strings.isBlank("\t\n\r "));
    }

    @Test
    void treatsAnyVisibleCharacterAsNotBlank() {
        assertFalse(Strings.isBlank("a"));
        assertFalse(Strings.isBlank("  a  "));
        assertTrue(Strings.isNotBlank("USDT.Tron"));
        assertFalse(Strings.isNotBlank("   "));
    }

    @Test
    void encodesBytesAsLowerCaseHexTwoCharactersEach() {
        assertEquals("00", Strings.toHex(new byte[] {0}));
        assertEquals("0f", Strings.toHex(new byte[] {15}));
        assertEquals("ff", Strings.toHex(new byte[] {(byte) 255}));
        assertEquals("deadbeef", Strings.toHex(new byte[] {(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF}));
        assertEquals("", Strings.toHex(new byte[0]));
    }

    /**
     * Guards the webhook signature format: a SHA-256 digest must always render as 64 hex characters,
     * so a leading zero byte cannot be dropped.
     */
    @Test
    void encodesADigestAsSixtyFourCharacters() throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256").digest("payload".getBytes(StandardCharsets.UTF_8));

        assertEquals(64, Strings.toHex(digest).length());
    }
}
