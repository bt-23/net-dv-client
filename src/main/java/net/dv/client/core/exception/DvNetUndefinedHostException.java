package net.dv.client.core.exception;

/**
 * Raised when a request is executed without a merchant API host, neither on the client nor on the
 * request options.
 */
public class DvNetUndefinedHostException extends DvNetException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception for a missing host.
     *
     * @param message The error message describing the exception.
     */
    public DvNetUndefinedHostException(final String message) {
        super(message);
    }
}
