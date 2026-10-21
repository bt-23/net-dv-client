package net.dv.client.services.webhooks.model;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * A payment that entered the mempool but has not been confirmed yet. The funds are not credited at
 * this point, so treat it as a hint that a payment is on its way rather than as a settlement.
 *
 * <p>On the wire every member of this payload, nested ones included, is prefixed with
 * {@code unconfirmed_}, and {@code unconfirmed_status} still reads {@code completed}; the mapper
 * strips the prefix.</p>
 */
public final class PaymentNotConfirmedEvent implements WebhookEvent {

    private final WebhookType type;
    private final String rawType;
    private final String status;
    private final String amount;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime paidAt;
    private final WebhookTransaction transaction;
    private final WebhookWallet wallet;

    /**
     * Constructs a {@code PaymentNotConfirmedEvent}.
     *
     * @param type        The kind of event, always {@link WebhookType#PAYMENT_NOT_CONFIRMED}.
     * @param rawType     The raw wire value of the type.
     * @param status      The transaction status, {@code completed} on the wire.
     * @param amount      The payment amount in USD.
     * @param createdAt   The moment DV.net recorded the transaction.
     * @param paidAt      The moment the transaction was created on the blockchain.
     * @param transaction The blockchain transaction that carried the payment.
     * @param wallet      The wallet the payment was made to.
     */
    public PaymentNotConfirmedEvent(final WebhookType type,
                                    final String rawType,
                                    final String status,
                                    final String amount,
                                    final OffsetDateTime createdAt,
                                    final OffsetDateTime paidAt,
                                    final WebhookTransaction transaction,
                                    final WebhookWallet wallet) {
        this.type = type;
        this.rawType = rawType;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.transaction = transaction;
        this.wallet = wallet;
    }

    /**
     * Returns the kind of event, always {@link WebhookType#PAYMENT_NOT_CONFIRMED}.
     *
     * @return The kind of event, always {@link WebhookType#PAYMENT_NOT_CONFIRMED}.
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
     * Returns the transaction status, {@code completed} on the wire; it does not mean the payment
     * is confirmed.
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

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PaymentNotConfirmedEvent)) {
            return false;
        }

        PaymentNotConfirmedEvent that = (PaymentNotConfirmedEvent) other;

        return Objects.equals(type, that.type)
                && Objects.equals(rawType, that.rawType)
                && Objects.equals(status, that.status)
                && Objects.equals(amount, that.amount)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(paidAt, that.paidAt)
                && Objects.equals(transaction, that.transaction)
                && Objects.equals(wallet, that.wallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rawType, status, amount, createdAt, paidAt, transaction, wallet);
    }

    @Override
    public String toString() {
        return "PaymentNotConfirmedEvent{"
                + "type=" + type
                + ", rawType=" + rawType
                + ", status=" + status
                + ", amount=" + amount
                + ", createdAt=" + createdAt
                + ", paidAt=" + paidAt
                + ", transaction=" + transaction
                + ", wallet=" + wallet
                + '}';
    }
}
