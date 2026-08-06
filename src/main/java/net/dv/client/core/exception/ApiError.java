package net.dv.client.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A single validation or business error entry returned by the DV.net API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiError {

    @JsonProperty("field")
    private String field;

    @JsonProperty("message")
    private String message;

    /**
     * Constructs an empty error, used by the JSON deserializer.
     */
    public ApiError() {
    }

    /**
     * Constructs an error with the given field and message.
     *
     * @param field   The request field the error refers to, may be {@code null}.
     * @param message The human readable error message.
     */
    public ApiError(final String field, final String message) {
        this.field = field;
        this.message = message;
    }

    /**
     * Returns the request field the error refers to.
     *
     * @return The field name, or {@code null} when the error is not field specific.
     */
    public String getField() {
        return field;
    }

    /**
     * Returns the human readable error message.
     *
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return field == null || field.isEmpty() ? String.valueOf(message) : field + ": " + message;
    }
}
