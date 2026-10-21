package net.dv.client.core.helper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

/**
 * Parses the timestamp formats used across the DV.net API and webhooks.
 *
 * <p>API responses carry RFC 3339 timestamps with an explicit offset
 * ({@code 2025-03-17T12:57:52.449795Z}). Webhook payloads never carry an offset
 * ({@code 2025-10-06T12:39:39.457399475}): the server stores them in {@code timestamp} columns
 * without time zone, and they are read as UTC.</p>
 *
 * <p>The server serialises an unset non nullable timestamp as the Go zero time
 * {@code 0001-01-01T00:00:00Z}, for example the {@code deleted_at} of an active address. That
 * value is reported as {@code null}.</p>
 */
public final class DateTimes {

    private static final OffsetDateTime GO_ZERO_TIME = OffsetDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    private DateTimes() {
    }

    /**
     * Parses a DV.net timestamp.
     *
     * @param value The timestamp to parse, may be {@code null} or blank.
     * @return The parsed timestamp, or {@code null} when the value is absent or the Go zero time.
     * @throws DateTimeParseException If the value matches none of the supported formats.
     */
    public static OffsetDateTime parse(final String value) {
        if (Strings.isBlank(value)) {
            return null;
        }

        OffsetDateTime parsed;
        try {
            parsed = OffsetDateTime.parse(value);
        } catch (DateTimeParseException withoutOffset) {
            parsed = LocalDateTime.parse(value).atOffset(ZoneOffset.UTC);
        }

        return parsed.isEqual(GO_ZERO_TIME) ? null : parsed;
    }
}
