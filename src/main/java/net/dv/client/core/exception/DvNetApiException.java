package net.dv.client.core.exception;

import java.util.Collections;
import java.util.List;

/**
 * Raised when the DV.net API answers with a non successful HTTP status code.
 *
 * <p>The raw response body is always preserved, and the {@code errors} array of the standard API
 * error envelope is decoded when present.</p>
 */
public class DvNetApiException extends DvNetException {

    private static final long serialVersionUID = 1L;

    /**
     * The HTTP status code of the response that triggered this exception.
     */
    private final int statusCode;

    /**
     * The raw response body returned by the API.
     */
    private final String responseBody;

    /**
     * The decoded error entries, empty when the body could not be decoded.
     */
    private final List<ApiError> errors;

    /**
     * Constructs an exception from an unsuccessful API response.
     *
     * @param statusCode   The HTTP status code.
     * @param responseBody The raw response body.
     */
    public DvNetApiException(final int statusCode, final String responseBody) {
        this(ApiErrorParser.buildMessage(statusCode, responseBody), statusCode, responseBody);
    }

    /**
     * Constructs an exception with an explicit message.
     *
     * @param message      The error message describing the exception.
     * @param statusCode   The HTTP status code.
     * @param responseBody The raw response body.
     */
    public DvNetApiException(final String message, final int statusCode, final String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.errors = ApiErrorParser.parseErrors(responseBody);
    }

    /**
     * Returns the HTTP status code of the failing response.
     *
     * @return The HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the raw response body of the failing response.
     *
     * @return The response body, possibly {@code null}.
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Returns the decoded error entries of the failing response.
     *
     * @return An unmodifiable list of errors, empty when none could be decoded.
     */
    public List<ApiError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{statusCode=" + statusCode + ", message='" + getMessage() + "'}";
    }
}
