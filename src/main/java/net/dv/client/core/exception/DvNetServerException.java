package net.dv.client.core.exception;

/**
 * Raised when the API fails with a 5xx status code.
 */
public class DvNetServerException extends DvNetApiException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an exception from an unsuccessful server side response.
     *
     * @param statusCode   The HTTP status code.
     * @param responseBody The raw response body.
     */
    public DvNetServerException(final int statusCode, final String responseBody) {
        super(statusCode, responseBody);
    }
}
