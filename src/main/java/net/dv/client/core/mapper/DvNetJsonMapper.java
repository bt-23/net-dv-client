package net.dv.client.core.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv.client.core.exception.DvNetInvalidResponseDataException;
import net.dv.client.core.helper.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Jackson backed implementation of {@link JsonMapper}.
 */
public class DvNetJsonMapper implements JsonMapper {

    /**
     * The member of the response envelope carrying the payload.
     */
    private static final String DATA_MEMBER = "data";

    private final ObjectMapper mapper;

    /**
     * Constructs a mapper with the conventions used by the DV.net API: {@code null} members are
     * omitted when serializing and unknown members are ignored when deserializing, so that new API
     * fields never break an existing integration.
     */
    public DvNetJsonMapper() {
        this.mapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new DvNetTimeModule())
                .build();
    }

    /**
     * Constructs a mapper on top of an existing Jackson mapper, for the rare case where the
     * application needs to control the Jackson configuration itself.
     *
     * @param mapper The Jackson mapper to delegate to.
     */
    public DvNetJsonMapper(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String writeValue(final Object value) throws DvNetInvalidResponseDataException {
        try {
            return mapper.writeValueAsString(value);
        } catch (JacksonException e) {
            throw new DvNetInvalidResponseDataException("Unable to serialize " + value.getClass().getName(), e);
        }
    }

    @Override
    public <T> T readData(final String json, final Class<T> type) throws DvNetInvalidResponseDataException {
        JsonNode data = extractData(json);

        try {
            return mapper.treeToValue(data, type);
        } catch (JacksonException e) {
            throw new DvNetInvalidResponseDataException("Unable to decode the response as " + type.getSimpleName(), e);
        }
    }

    @Override
    public <T> List<T> readDataList(final String json, final Class<T> elementType)
            throws DvNetInvalidResponseDataException {
        JsonNode data = extractData(json);

        if (data.isNull()) {
            return new ArrayList<T>();
        }

        if (!data.isArray()) {
            throw new DvNetInvalidResponseDataException("The response data member is not an array.");
        }

        List<T> elements = new ArrayList<T>(data.size());

        try {
            for (JsonNode element : data) {
                elements.add(mapper.treeToValue(element, elementType));
            }
        } catch (JacksonException e) {
            throw new DvNetInvalidResponseDataException(
                    "Unable to decode the response as a list of " + elementType.getSimpleName(), e);
        }

        return elements;
    }

    @Override
    public JsonNode readTree(final String json) throws DvNetInvalidResponseDataException {
        if (Strings.isBlank(json)) {
            throw new DvNetInvalidResponseDataException("The payload is empty.");
        }

        try {
            return mapper.readTree(json);
        } catch (JacksonException e) {
            throw new DvNetInvalidResponseDataException("The payload is not valid JSON.", e);
        }
    }

    private JsonNode extractData(final String json) throws DvNetInvalidResponseDataException {
        JsonNode root = readTree(json);
        JsonNode data = root.get(DATA_MEMBER);

        if (data == null) {
            throw new DvNetInvalidResponseDataException("The response does not contain a data member.");
        }

        return data;
    }
}
