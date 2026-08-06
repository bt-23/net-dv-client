package net.dv.client.core.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv.client.core.exception.DvNetInvalidResponseDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DvNetJsonMapperTest {

    private static final class Sample {

        @JsonProperty("store_external_id")
        private final String storeExternalId;

        @JsonProperty("amount")
        private final String amount;

        @JsonProperty("created_at")
        private final OffsetDateTime createdAt;

        @JsonCreator
        Sample(@JsonProperty("store_external_id") final String storeExternalId,
               @JsonProperty("amount") final String amount,
               @JsonProperty("created_at") final OffsetDateTime createdAt) {
            this.storeExternalId = storeExternalId;
            this.amount = amount;
            this.createdAt = createdAt;
        }

        Sample(final String storeExternalId, final String amount) {
            this(storeExternalId, amount, null);
        }
    }

    private DvNetJsonMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DvNetJsonMapper();
    }

    @Test
    void writeValueOmitsNullMembers() throws Exception {
        String json = mapper.writeValue(new Sample("customer-42", null));

        assertTrue(json.contains("\"store_external_id\":\"customer-42\""));
        assertFalse(json.contains("amount"));
    }

    @Test
    void readDataUnwrapsTheResponseEnvelope() throws Exception {
        String body = "{\"code\":200,\"message\":\"ok\",\"data\":{\"store_external_id\":\"customer-42\"}}";

        assertEquals("customer-42", mapper.readData(body, Sample.class).storeExternalId);
    }

    @Test
    void readDataIgnoresMembersTheClientDoesNotKnowAbout() throws Exception {
        String body = "{\"data\":{\"store_external_id\":\"customer-42\",\"introduced_later\":true}}";

        assertEquals("customer-42", mapper.readData(body, Sample.class).storeExternalId);
    }

    @Test
    void readDataListDecodesEveryElement() throws Exception {
        String body = "{\"data\":[{\"amount\":\"1\"},{\"amount\":\"2\"}]}";

        List<Sample> samples = mapper.readDataList(body, Sample.class);

        assertEquals(2, samples.size());
        assertEquals("1", samples.get(0).amount);
        assertEquals("2", samples.get(1).amount);
    }

    @Test
    void readDataListTreatsANullPayloadAsEmpty() throws Exception {
        assertTrue(mapper.readDataList("{\"data\":null}", Sample.class).isEmpty());
    }

    @Test
    void readDataListRejectsAPayloadThatIsNotAnArray() {
        assertThrows(DvNetInvalidResponseDataException.class,
                () -> mapper.readDataList("{\"data\":{}}", Sample.class));
    }

    @Test
    void readDataRejectsAResponseWithoutADataMember() {
        DvNetInvalidResponseDataException exception = assertThrows(DvNetInvalidResponseDataException.class,
                () -> mapper.readData("{\"code\":200}", Sample.class));

        assertTrue(exception.getMessage().contains("data member"));
    }

    @Test
    void readDataRejectsABodyThatIsNotJson() {
        assertThrows(DvNetInvalidResponseDataException.class, () -> mapper.readData("<html/>", Sample.class));
    }

    @Test
    void readTreeRejectsAnEmptyBody() {
        assertThrows(DvNetInvalidResponseDataException.class, () -> mapper.readTree(""));
        assertThrows(DvNetInvalidResponseDataException.class, () -> mapper.readTree("   "));
        assertThrows(DvNetInvalidResponseDataException.class, () -> mapper.readTree(null));
    }

    @Test
    void readTreeParsesADocumentWithoutAnEnvelope() throws Exception {
        assertEquals("PaymentReceived", mapper.readTree("{\"type\":\"PaymentReceived\"}").get("type").asText());
    }

    @Test
    void readsATimestampCarryingAnOffset() throws Exception {
        String body = "{\"data\":{\"created_at\":\"2025-03-17T12:57:52.449795Z\"}}";

        assertEquals(OffsetDateTime.parse("2025-03-17T12:57:52.449795Z"),
                mapper.readData(body, Sample.class).createdAt);
    }

    /**
     * Some webhook payloads omit the offset, which the standard Jackson time module rejects. The
     * client reads those as UTC, so the same mapper serves API and webhook bodies alike.
     */
    @Test
    void readsATimestampWithoutAnOffsetAsUtc() throws Exception {
        String body = "{\"data\":{\"created_at\":\"2025-10-06T12:39:39.457399475\"}}";

        OffsetDateTime createdAt = mapper.readData(body, Sample.class).createdAt;

        assertEquals(ZoneOffset.UTC, createdAt.getOffset());
        assertEquals(2025, createdAt.getYear());
    }

    @Test
    void readsAnAbsentTimestampAsNull() throws Exception {
        assertNull(mapper.readData("{\"data\":{\"created_at\":null}}", Sample.class).createdAt);
    }

    @Test
    void rejectsATimestampInAnUnknownFormat() {
        assertThrows(DvNetInvalidResponseDataException.class,
                () -> mapper.readData("{\"data\":{\"created_at\":\"17/03/2025\"}}", Sample.class));
    }

    @Test
    void writesATimestampBackInIsoFormat() throws Exception {
        String json = mapper.writeValue(
                new Sample("customer-42", "1", OffsetDateTime.parse("2025-03-17T12:57:52Z")));

        assertTrue(json.contains("\"created_at\":\"2025-03-17T12:57:52Z\""));
    }
}
