package net.dv.client.services.webhooks.model;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The blockchain transaction a webhook reports.
 *
 * <p>{@code txHash} together with {@code bcUniqKey} identifies the event uniquely. DV.net retries a
 * webhook up to thirty times until it is acknowledged, so store that pair and ignore an event you
 * have already processed.</p>
 */
public final class WebhookTransaction {

    private final String txId;
    private final String txHash;
    private final String bcUniqKey;
    private final String currency;
    private final String currencyId;
    private final String blockchain;
    private final String amount;
    private final String amountUsd;
    private final OffsetDateTime createdAt;

    /**
     * Constructs a {@code WebhookTransaction}.
     *
     * @param txId       The transaction identifier in the merchant system.
     * @param txHash     The blockchain transaction hash.
     * @param bcUniqKey  The unique event key within the transaction.
     * @param currency   The currency ticker, for example {@code LTC}.
     * @param currencyId The currency identifier, for example {@code LTC.Litecoin}.
     * @param blockchain The blockchain the transaction happened on.
     * @param amount     The amount in the currency itself.
     * @param amountUsd  The same amount converted to USD.
     * @param createdAt  The moment the transaction was recorded.
     */
    public WebhookTransaction(final String txId,
                              final String txHash,
                              final String bcUniqKey,
                              final String currency,
                              final String currencyId,
                              final String blockchain,
                              final String amount,
                              final String amountUsd,
                              final OffsetDateTime createdAt) {
        this.txId = txId;
        this.txHash = txHash;
        this.bcUniqKey = bcUniqKey;
        this.currency = currency;
        this.currencyId = currencyId;
        this.blockchain = blockchain;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.createdAt = createdAt;
    }

    /**
     * Returns the transaction identifier in the merchant system.
     *
     * @return The transaction identifier in the merchant system.
     */
    public String getTxId() {
        return txId;
    }

    /**
     * Returns the blockchain transaction hash.
     *
     * @return The blockchain transaction hash.
     */
    public String getTxHash() {
        return txHash;
    }

    /**
     * Returns the unique event key within the transaction.
     *
     * @return The unique event key within the transaction.
     */
    public String getBcUniqKey() {
        return bcUniqKey;
    }

    /**
     * Returns the currency ticker, for example {@code LTC}.
     *
     * @return The currency ticker, for example {@code LTC}.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the currency identifier, for example {@code LTC.Litecoin}.
     *
     * @return The currency identifier, for example {@code LTC.Litecoin}.
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Returns the blockchain the transaction happened on.
     *
     * @return The blockchain the transaction happened on.
     */
    public String getBlockchain() {
        return blockchain;
    }

    /**
     * Returns the amount in the currency itself.
     *
     * @return The amount in the currency itself.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Returns the same amount converted to USD.
     *
     * @return The same amount converted to USD.
     */
    public String getAmountUsd() {
        return amountUsd;
    }

    /**
     * Returns the moment the transaction was recorded.
     *
     * @return The moment the transaction was recorded.
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WebhookTransaction)) {
            return false;
        }

        WebhookTransaction that = (WebhookTransaction) other;

        return Objects.equals(txId, that.txId)
                && Objects.equals(txHash, that.txHash)
                && Objects.equals(bcUniqKey, that.bcUniqKey)
                && Objects.equals(currency, that.currency)
                && Objects.equals(currencyId, that.currencyId)
                && Objects.equals(blockchain, that.blockchain)
                && Objects.equals(amount, that.amount)
                && Objects.equals(amountUsd, that.amountUsd)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txId, txHash, bcUniqKey, currency, currencyId, blockchain, amount, amountUsd, createdAt);
    }

    @Override
    public String toString() {
        return "WebhookTransaction{"
                + "txId=" + txId
                + ", txHash=" + txHash
                + ", bcUniqKey=" + bcUniqKey
                + ", currency=" + currency
                + ", currencyId=" + currencyId
                + ", blockchain=" + blockchain
                + ", amount=" + amount
                + ", amountUsd=" + amountUsd
                + ", createdAt=" + createdAt
                + '}';
    }
}
