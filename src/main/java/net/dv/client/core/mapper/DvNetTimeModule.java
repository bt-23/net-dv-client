package net.dv.client.core.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.dv.client.core.helper.DateTimes;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

/**
 * Teaches Jackson the timestamp handling of the DV.net API.
 *
 * <p>This deliberately replaces what {@code jackson-datatype-jsr310} would do, for two reasons: it
 * keeps the client down to the three Jackson artifacts, and it routes every timestamp through
 * {@link DateTimes}, so a response body and a webhook body are read by exactly the same rules. The
 * standard module would reject the offset-less timestamps some webhooks carry.</p>
 */
public final class DvNetTimeModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs the module.
     */
    public DvNetTimeModule() {
        super("dv-net-time");

        addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
        addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
    }

    private static final class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

        @Override
        public OffsetDateTime deserialize(final JsonParser parser, final DeserializationContext context)
                throws IOException {
            String value = parser.getValueAsString();

            try {
                return DateTimes.parse(value);
            } catch (DateTimeParseException e) {
                throw context.weirdStringException(value, OffsetDateTime.class,
                        "expected an RFC 3339 timestamp, with or without an offset");
            }
        }
    }

    private static final class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

        @Override
        public void serialize(final OffsetDateTime value, final JsonGenerator generator,
                              final SerializerProvider provider) throws IOException {
            generator.writeString(value.toString());
        }
    }
}
