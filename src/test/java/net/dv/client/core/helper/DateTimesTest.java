package net.dv.client.core.helper;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateTimesTest {

    @Test
    void parsesAUtcTimestamp() {
        assertEquals(OffsetDateTime.parse("2025-03-17T12:57:52.449795Z"),
                DateTimes.parse("2025-03-17T12:57:52.449795Z"));
    }

    @Test
    void parsesATimestampWithAnExplicitOffset() {
        assertEquals(OffsetDateTime.parse("2025-01-31T07:21:32.292335839+03:00"),
                DateTimes.parse("2025-01-31T07:21:32.292335839+03:00"));
    }

    @Test
    void readsATimestampWithoutAnOffsetAsUtc() {
        assertEquals(OffsetDateTime.parse("2025-10-06T12:39:39.457399475Z"),
                DateTimes.parse("2025-10-06T12:39:39.457399475"));
    }

    @Test
    void returnsNullForAnAbsentValue() {
        assertNull(DateTimes.parse(null));
        assertNull(DateTimes.parse(""));
        assertNull(DateTimes.parse("   "));
    }

    @Test
    void rejectsAValueThatIsNotATimestamp() {
        assertThrows(DateTimeParseException.class, () -> DateTimes.parse("yesterday"));
    }
}
