package net.dv.client.core.exception;

/**
 * Raised when a request is executed without a store API key, neither on the client nor on the
 * request options.
 */
public class DvNetUndefinedApiKeyException extends DvNetException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception for a missing API key.
     *
     * @param message The error message describing the exception.
     */
    public DvNetUndefinedApiKeyException(final String message) {
        super(message);
    }
}
