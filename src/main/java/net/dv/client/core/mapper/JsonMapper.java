package net.dv.client.core.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv.client.core.exception.DvNetInvalidResponseDataException;

import java.util.List;

/**
 * Converts between DV.net JSON payloads and the client model classes.
 *
 * <p>Every successful API response is wrapped in a {@code {"code": .., "data": .., "message": ..}}
 * envelope, so the read methods unwrap the {@code data} member before binding it.</p>
 */
public interface JsonMapper {

    /**
     * Serializes an object to its JSON representation.
     *
     * @param value The object to serialize.
     * @return The JSON representation of the object.
     * @throws DvNetInvalidResponseDataException If the object cannot be serialized.
     */
    String writeValue(Object value) throws DvNetInvalidResponseDataException;

    /**
     * Reads the {@code data} member of an API response envelope into the given type.
     *
     * @param json The raw response body.
     * @param type The type to bind the {@code data} member to.
     * @param <T>  The type of the resulting object.
     * @return The decoded object.
     * @throws DvNetInvalidResponseDataException If the body is not valid JSON, carries no
     *                                           {@code data} member, or cannot be bound.
     */
    <T> T readData(String json, Class<T> type) throws DvNetInvalidResponseDataException;

    /**
     * Reads the {@code data} member of an API response envelope into a list of the given type.
     *
     * @param json        The raw response body.
     * @param elementType The type of the list elements.
     * @param <T>         The type of the list elements.
     * @return The decoded list, empty when {@code data} is JSON {@code null}.
     * @throws DvNetInvalidResponseDataException If the body is not valid JSON, carries no
     *                                           {@code data} member, or cannot be bound.
     */
    <T> List<T> readDataList(String json, Class<T> elementType) throws DvNetInvalidResponseDataException;

    /**
     * Parses a raw JSON document into a tree, without expecting any envelope.
     *
     * @param json The raw JSON document.
     * @return The parsed tree.
     * @throws DvNetInvalidResponseDataException If the document is not valid JSON.
     */
    JsonNode readTree(String json) throws DvNetInvalidResponseDataException;
}
