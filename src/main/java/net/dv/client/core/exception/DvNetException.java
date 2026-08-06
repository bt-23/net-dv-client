package net.dv.client.core.exception;

/**
 * Base exception for every error raised by the DV.net client.
 *
 * <p>Catching this type is enough to handle any failure produced by the SDK, whether it originated
 * from configuration, the network, the API itself or response decoding.</p>
 */
public class DvNetException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the given message.
     *
     * @param message The error message describing the exception.
     */
    public DvNetException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the given message and cause.
     *
     * @param message The error message describing the exception.
     * @param cause   The cause of the exception.
     */
    public DvNetException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
