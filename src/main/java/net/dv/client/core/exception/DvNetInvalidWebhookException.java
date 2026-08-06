package net.dv.client.core.exception;

/**
 * Raised when an incoming webhook payload is not valid JSON or does not match any known DV.net
 * webhook shape.
 */
public class DvNetInvalidWebhookException extends DvNetException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new webhook exception.
     *
     * @param message The error message describing the exception.
     */
    public DvNetInvalidWebhookException(final String message) {
        super(message);
    }

    /**
     * Constructs a new webhook exception with a cause.
     *
     * @param message The error message describing the exception.
     * @param cause   The underlying failure.
     */
    public DvNetInvalidWebhookException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
