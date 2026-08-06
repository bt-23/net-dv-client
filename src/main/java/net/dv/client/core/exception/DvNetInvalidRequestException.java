package net.dv.client.core.exception;

/**
 * Raised when the API rejects the request with a 4xx status code, for example because of a missing
 * parameter, an unknown resource or an invalid API key.
 */
public class DvNetInvalidRequestException extends DvNetApiException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an exception from an unsuccessful client side response.
     *
     * @param statusCode   The HTTP status code.
     * @param responseBody The raw response body.
     */
    public DvNetInvalidRequestException(final int statusCode, final String responseBody) {
        super(statusCode, responseBody);
    }
}
