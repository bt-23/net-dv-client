package net.dv.client.services.webhooks.model;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * A payment that arrived on chain but was held back by the anti money laundering screening. The
 * funds are not credited to the store, so the order must not be fulfilled.
 *
 * <p>DV.net delivers this event to the endpoints subscribed to {@code PaymentNotConfirmed}; there
 * is no separate subscription for it.</p>
 */
public final class PaymentAmlBlockedEvent implements WebhookEvent {

    private final WebhookType type;
    private final String rawType;
    private final String status;
    private final String amount;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime paidAt;
    private final WebhookTransaction transaction;
    private final WebhookWallet wallet;
    private final AmlCheck amlCheck;

    /**
     * Constructs a {@code PaymentAmlBlockedEvent}.
     *
     * @param type        The kind of event, always {@link WebhookType#PAYMENT_AML_BLOCKED}.
     * @param rawType     The raw wire value of the type.
     * @param status      The transaction status, {@code completed} on the wire.
     * @param amount      The payment amount in USD.
     * @param createdAt   The moment DV.net recorded the transaction.
     * @param paidAt      The moment the transaction was created on the blockchain.
     * @param transaction The blockchain transaction that carried the payment.
     * @param wallet      The wallet the payment was made to.
     * @param amlCheck    The screening that blocked the payment.
     */
    public PaymentAmlBlockedEvent(final WebhookType type,
                                  final String rawType,
                                  final String status,
                                  final String amount,
                                  final OffsetDateTime createdAt,
                                  final OffsetDateTime paidAt,
                                  final WebhookTransaction transaction,
                                  final WebhookWallet wallet,
                                  final AmlCheck amlCheck) {
        this.type = type;
        this.rawType = rawType;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.transaction = transaction;
        this.wallet = wallet;
        this.amlCheck = amlCheck;
    }

    /**
     * Returns the kind of event, always {@link WebhookType#PAYMENT_AML_BLOCKED}.
     *
     * @return The kind of event.
     */
    @Override
    public WebhookType getType() {
        return type;
    }

    /**
     * Returns the raw wire value of the type.
     *
     * @return The raw wire value of the type.
     */
    @Override
    public String getRawType() {
        return rawType;
    }

    /**
     * Returns the transaction status, {@code completed} on the wire. It describes the blockchain
     * transaction, not the outcome of the screening.
     *
     * @return The transaction status.
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * Returns the payment amount in USD.
     *
     * @return The payment amount in USD.
     */
    @Override
    public String getAmount() {
        return amount;
    }

    /**
     * Returns the moment DV.net recorded the transaction.
     *
     * @return The moment DV.net recorded the transaction.
     */
    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the moment the transaction was created on the blockchain.
     *
     * @return The moment the transaction was created on the blockchain.
     */
    @Override
    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    /**
     * Returns the blockchain transaction that carried the payment.
     *
     * @return The blockchain transaction that carried the payment.
     */
    @Override
    public WebhookTransaction getTransaction() {
        return transaction;
    }

    /**
     * Returns the wallet the payment was made to.
     *
     * @return The wallet the payment was made to.
     */
    @Override
    public WebhookWallet getWallet() {
        return wallet;
    }

    /**
     * Returns the screening that blocked the payment.
     *
     * @return The screening that blocked the payment, {@code null} when the payload carried none.
     */
    public AmlCheck getAmlCheck() {
        return amlCheck;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PaymentAmlBlockedEvent)) {
            return false;
        }

        PaymentAmlBlockedEvent that = (PaymentAmlBlockedEvent) other;

        return Objects.equals(type, that.type)
                && Objects.equals(rawType, that.rawType)
                && Objects.equals(status, that.status)
                && Objects.equals(amount, that.amount)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(paidAt, that.paidAt)
                && Objects.equals(transaction, that.transaction)
                && Objects.equals(wallet, that.wallet)
                && Objects.equals(amlCheck, that.amlCheck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rawType, status, amount, createdAt, paidAt, transaction, wallet, amlCheck);
    }

    @Override
    public String toString() {
        return "PaymentAmlBlockedEvent{"
                + "type=" + type
                + ", rawType=" + rawType
                + ", status=" + status
                + ", amount=" + amount
                + ", createdAt=" + createdAt
                + ", paidAt=" + paidAt
                + ", transaction=" + transaction
                + ", wallet=" + wallet
                + ", amlCheck=" + amlCheck
                + '}';
    }
}
