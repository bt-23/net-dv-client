package net.dv.client.core.exception;

/**
 * Raised when a successful response cannot be decoded, either because it is not valid JSON or
 * because it does not carry the expected {@code data} envelope.
 */
public class DvNetInvalidResponseDataException extends DvNetException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new decoding exception.
     *
     * @param message The error message describing the exception.
     */
    public DvNetInvalidResponseDataException(final String message) {
        super(message);
    }

    /**
     * Constructs a new decoding exception with a cause.
     *
     * @param message The error message describing the exception.
     * @param cause   The underlying decoding failure.
     */
    public DvNetInvalidResponseDataException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
