package net.dv.client.core.helper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

/**
 * Parses the timestamp formats used across the DV.net API and webhooks.
 *
 * <p>Most timestamps are RFC 3339 with an explicit offset ({@code 2025-03-17T12:57:52.449795Z} or
 * {@code 2025-01-31T07:21:32.292335839+03:00}), but webhook payloads may also carry a local
 * timestamp without any offset ({@code 2025-10-06T12:39:39.457399475}), which is read as UTC.</p>
 */
public final class DateTimes {

    private DateTimes() {
    }

    /**
     * Parses a DV.net timestamp.
     *
     * @param value The timestamp to parse, may be {@code null} or blank.
     * @return The parsed timestamp, or {@code null} when the value is absent.
     * @throws DateTimeParseException If the value matches none of the supported formats.
     */
    public static OffsetDateTime parse(final String value) {
        if (Strings.isBlank(value)) {
            return null;
        }

        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException withoutOffset) {
            return LocalDateTime.parse(value).atOffset(ZoneOffset.UTC);
        }
    }
}
