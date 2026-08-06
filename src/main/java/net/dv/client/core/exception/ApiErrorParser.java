package net.dv.client.core.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import net.dv.client.core.helper.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Decodes the standard DV.net error envelope {@code {"code": 400, "errors": [{"field": "...",
 * "message": "..."}]}} so that failures can be reported with actionable detail.
 */
final class ApiErrorParser {

    private static final ObjectMapper MAPPER = JsonMapper.builder().build();

    private ApiErrorParser() {
    }

    /**
     * Extracts the error entries of an API error body.
     *
     * @param responseBody The raw response body.
     * @return The decoded errors, empty when the body carries none.
     */
    static List<ApiError> parseErrors(final String responseBody) {
        List<ApiError> errors = new ArrayList<>();

        if (Strings.isBlank(responseBody)) {
            return errors;
        }

        try {
            JsonNode root = MAPPER.readTree(responseBody);
            JsonNode errorsNode = root.get("errors");

            if (errorsNode != null && errorsNode.isArray()) {
                for (JsonNode errorNode : errorsNode) {
                    errors.add(new ApiError(text(errorNode, "field"), text(errorNode, "message")));
                }
            } else if (root.has("message")) {
                errors.add(new ApiError(null, text(root, "message")));
            }
        } catch (IOException | RuntimeException ignored) {
            // The body is not the documented JSON envelope, the raw body remains available.
        }

        return errors;
    }

    /**
     * Builds a readable message combining the status code and the decoded errors.
     *
     * @param statusCode   The HTTP status code.
     * @param responseBody The raw response body.
     * @return The exception message.
     */
    static String buildMessage(final int statusCode, final String responseBody) {
        List<ApiError> errors = parseErrors(responseBody);

        if (errors.isEmpty()) {
            String body = Strings.isBlank(responseBody) ? "no response body" : responseBody;
            return "DV.net request failed with status " + statusCode + ": " + body;
        }

        StringBuilder message = new StringBuilder("DV.net request failed with status ")
                .append(statusCode)
                .append(": ");

        for (int i = 0; i < errors.size(); i++) {
            if (i > 0) {
                message.append("; ");
            }
            message.append(errors.get(i));
        }

        return message.toString();
    }

    private static String text(final JsonNode node, final String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }
}
