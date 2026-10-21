package net.dv.client.services.webhooks.model;

import java.time.OffsetDateTime;

/**
 * A decoded DV.net webhook.
 *
 * <p>Four implementations exist: {@link PaymentReceivedEvent}, {@link PaymentNotConfirmedEvent},
 * {@link PaymentAmlBlockedEvent} and {@link WithdrawalEvent}. Tell them apart with
 * {@code instanceof}, or branch on {@link #getType()} when you only care about the kind of
 * event.</p>
 *
 * <p>Webhook timestamps carry no offset on the wire and are read as UTC.</p>
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
     * Returns the status of the transaction. Deposit events always carry {@code completed};
     * withdrawal events carry no status at all.
     *
     * @return The status, or {@code null} when the payload carried none.
     */
    String getStatus();

    /**
     * Returns the amount of the event in USD.
     *
     * @return The amount, as a decimal string.
     */
    String getAmount();

    /**
     * Returns the moment DV.net recorded the transaction.
     *
     * @return The creation timestamp, or {@code null} when the payload carried none.
     */
    OffsetDateTime getCreatedAt();

    /**
     * Returns the moment the transaction was created on the blockchain.
     *
     * @return The network timestamp, or {@code null} when the payload carried none.
     */
    OffsetDateTime getPaidAt();

    /**
     * Returns the blockchain transaction the event reports.
     *
     * @return The transaction.
     */
    WebhookTransaction getTransaction();

    /**
     * Returns the wallet the event refers to. Withdrawal events carry no wallet.
     *
     * @return The wallet, or {@code null} when the payload carried none.
     */
    WebhookWallet getWallet();
}
