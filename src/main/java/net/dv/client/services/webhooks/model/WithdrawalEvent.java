package net.dv.client.services.webhooks.model;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * A withdrawal from a processing wallet that reached the recipient.
 *
 * <p>Unlike the deposit events, the payload carries neither a {@code status} nor a {@code wallet}
 * member, so {@link #getStatus()} and {@link #getWallet()} return {@code null}. Use
 * {@link #getWithdrawalId()} to match it against the withdrawal you created.</p>
 */
public final class WithdrawalEvent implements WebhookEvent {

    private final WebhookType type;
    private final String rawType;
    private final String status;
    private final String amount;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime paidAt;
    private final WebhookTransaction transaction;
    private final WebhookWallet wallet;
    private final String withdrawalId;

    /**
     * Constructs a {@code WithdrawalEvent}.
     *
     * @param type         The kind of event, always {@link WebhookType#WITHDRAWAL_FROM_PROCESSING_RECEIVED}.
     * @param rawType      The raw wire value of the type.
     * @param status       The status, {@code null} as the server sends none for withdrawals.
     * @param amount       The withdrawal amount in USD.
     * @param createdAt    The moment DV.net recorded the transaction.
     * @param paidAt       The moment the transaction was created on the blockchain.
     * @param transaction  The blockchain transaction that carried the withdrawal.
     * @param wallet       The wallet, {@code null} as the server sends none for withdrawals.
     * @param withdrawalId The identifier of the withdrawal, as returned when it was created.
     */
    public WithdrawalEvent(final WebhookType type,
                           final String rawType,
                           final String status,
                           final String amount,
                           final OffsetDateTime createdAt,
                           final OffsetDateTime paidAt,
                           final WebhookTransaction transaction,
                           final WebhookWallet wallet,
                           final String withdrawalId) {
        this.type = type;
        this.rawType = rawType;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.transaction = transaction;
        this.wallet = wallet;
        this.withdrawalId = withdrawalId;
    }

    /**
     * Returns the kind of event, always {@link WebhookType#WITHDRAWAL_FROM_PROCESSING_RECEIVED}.
     *
     * @return The kind of event, always {@link WebhookType#WITHDRAWAL_FROM_PROCESSING_RECEIVED}.
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
     * Returns the status, which the server does not send for withdrawals.
     *
     * @return The status, {@code null} for the payloads DV.net currently sends.
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * Returns the withdrawal amount in USD.
     *
     * @return The withdrawal amount in USD.
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
     * Returns the blockchain transaction that carried the withdrawal.
     *
     * @return The blockchain transaction that carried the withdrawal.
     */
    @Override
    public WebhookTransaction getTransaction() {
        return transaction;
    }

    /**
     * Returns the wallet, which the server does not send for withdrawals.
     *
     * @return The wallet, {@code null} for the payloads DV.net currently sends.
     */
    @Override
    public WebhookWallet getWallet() {
        return wallet;
    }

    /**
     * Returns the identifier of the withdrawal, as returned when it was created.
     *
     * @return The withdrawal identifier.
     */
    public String getWithdrawalId() {
        return withdrawalId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WithdrawalEvent)) {
            return false;
        }

        WithdrawalEvent that = (WithdrawalEvent) other;

        return Objects.equals(type, that.type)
                && Objects.equals(rawType, that.rawType)
                && Objects.equals(status, that.status)
                && Objects.equals(amount, that.amount)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(paidAt, that.paidAt)
                && Objects.equals(transaction, that.transaction)
                && Objects.equals(wallet, that.wallet)
                && Objects.equals(withdrawalId, that.withdrawalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rawType, status, amount, createdAt, paidAt, transaction, wallet, withdrawalId);
    }

    @Override
    public String toString() {
        return "WithdrawalEvent{"
                + "type=" + type
                + ", rawType=" + rawType
                + ", status=" + status
                + ", amount=" + amount
                + ", createdAt=" + createdAt
                + ", paidAt=" + paidAt
                + ", transaction=" + transaction
                + ", wallet=" + wallet
                + ", withdrawalId=" + withdrawalId
                + '}';
    }
}
