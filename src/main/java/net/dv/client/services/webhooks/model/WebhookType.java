package net.dv.client.services.webhooks.model;

/**
 * The kinds of webhook DV.net delivers.
 */
public enum WebhookType {

    /** A payment reached the required number of confirmations and was credited. */
    PAYMENT_RECEIVED("PaymentReceived"),

    /** A payment entered the mempool and is waiting for confirmations. */
    PAYMENT_NOT_CONFIRMED("PaymentNotConfirmed"),

    /** A withdrawal from a processing wallet reached the recipient. */
    WITHDRAWAL_FROM_PROCESSING_RECEIVED("WithdrawalFromProcessingReceived"),

    /**
     * A payment was held back by the anti money laundering screening. Delivered to the endpoints
     * subscribed to {@link #PAYMENT_NOT_CONFIRMED}.
     */
    PAYMENT_AML_BLOCKED("PaymentAMLBlocked"),

    /** A type this version of the client does not know about yet. */
    UNKNOWN("");

    private final String value;

    WebhookType(final String value) {
        this.value = value;
    }

    /**
     * Returns the wire value of the type.
     *
     * @return The value carried by the {@code type} member of the payload.
     */
    public String getValue() {
        return value;
    }

    /**
     * Resolves a wire value to its enum constant.
     *
     * @param value The value carried by the payload.
     * @return The matching constant, or {@link #UNKNOWN} when the value is not recognised, so that
     *         a newly introduced webhook type never breaks an existing integration.
     */
    public static WebhookType fromValue(final String value) {
        for (WebhookType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }
}
