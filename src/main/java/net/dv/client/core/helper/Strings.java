package net.dv.client.core.helper;

/**
 * The string checks the client needs but Java 8 does not offer.
 */
public final class Strings {

    private Strings() {
    }

    /**
     * Tells whether a string carries no visible characters.
     *
     * @param value The string to check, may be {@code null}.
     * @return {@code true} when the value is {@code null}, empty, or whitespace only.
     */
    public static boolean isBlank(final String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Tells whether a string carries at least one visible character.
     *
     * @param value The string to check, may be {@code null}.
     * @return {@code true} when the value holds something other than whitespace.
     */
    public static boolean isNotBlank(final String value) {
        return !isBlank(value);
    }

    /**
     * Hex encodes a byte array, the encoding DV.net uses for webhook signatures.
     *
     * @param bytes The bytes to encode.
     * @return The lower case hex representation.
     */
    public static String toHex(final byte[] bytes) {
        char[] hex = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            hex[i * 2] = Character.forDigit(value >>> 4, 16);
            hex[i * 2 + 1] = Character.forDigit(value & 0x0F, 16);
        }

        return new String(hex);
    }
}
