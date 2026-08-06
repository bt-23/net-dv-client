package net.dv.client.core.exception;

/**
 * Raised when the request never reached the API, for example on a connection timeout or a DNS
 * failure.
 */
public class DvNetNetworkException extends DvNetException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new network exception.
     *
     * @param message The error message describing the exception.
     * @param cause   The underlying transport failure.
     */
    public DvNetNetworkException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
