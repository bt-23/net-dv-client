package net.dv.client.services.webhooks.model;

import java.time.OffsetDateTime;

/**
 * A decoded DV.net webhook.
 *
 * <p>Three implementations exist: {@link PaymentReceivedEvent}, {@link PaymentNotConfirmedEvent} and
 * {@link WithdrawalEvent}. Tell them apart with {@code instanceof}, or branch on {@link #getType()}
 * when you only care about the kind of event.</p>
 */
public interface WebhookEvent {

    /**
     * Returns the kind of event.
     *
     * @return The webhook type.
     */
    WebhookType getType();

    /**
     * Returns the raw wire value of the type, which is useful when the payload announces a type
     * this version of the client does not know about.
     *
     * @return The raw type value.
     */
    String getRawType();

    /**
     * Returns the status of the payment or withdrawal.
     *
     * @return The status, for example {@code completed}.
     */
    String getStatus();

    /**
     * Returns the amount of the event in USD.
     *
     * @return The amount, as a decimal string.
     */
    String getAmount();

    /**
     * Returns the moment the event was created.
     *
     * @return The creation timestamp.
     */
    OffsetDateTime getCreatedAt();

    /**
     * Returns the moment the payment or withdrawal settled.
     *
     * @return The settlement timestamp.
     */
    OffsetDateTime getPaidAt();

    /**
     * Returns the blockchain transaction the event reports.
     *
     * @return The transaction.
     */
    WebhookTransaction getTransaction();

    /**
     * Returns the wallet the event refers to.
     *
     * @return The wallet, or {@code null} when the payload carried none.
     */
    WebhookWallet getWallet();
}
