package net.dv.client.services.transactions.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * An incoming transfer that is visible in the mempool but has not reached the required number of
 * confirmations yet, so the funds are not credited.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UnconfirmedTransaction {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("tx_hash")
    private final String txHash;

    @JsonProperty("bc_uniq_key")
    private final String bcUniqKey;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("blockchain")
    private final String blockchain;

    @JsonProperty("currency_id")
    private final String currencyId;

    @JsonProperty("from_address")
    private final String fromAddress;

    @JsonProperty("to_address")
    private final String toAddress;

    @JsonProperty("amount")
    private final String amount;

    @JsonProperty("amount_usd")
    private final String amountUsd;

    @JsonProperty("network_created_at")
    private final OffsetDateTime networkCreatedAt;

    @JsonProperty("created_at")
    private final OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private final OffsetDateTime updatedAt;

    /**
     * Constructs a {@code UnconfirmedTransaction}.
     *
     * @param id               The transaction identifier in the merchant system.
     * @param txHash           The blockchain transaction hash.
     * @param bcUniqKey        The unique event key within the transaction, used together with {@code txHash} to
     *                        deduplicate events.
     * @param type             The transaction type.
     * @param blockchain       The blockchain the transfer happened on.
     * @param currencyId       The currency being transferred, for example {@code USDT.Tron}.
     * @param fromAddress      The sending address.
     * @param toAddress        The receiving address, one of your deposit addresses.
     * @param amount           The amount in the currency itself.
     * @param amountUsd        The same amount converted to USD, may be {@code null}.
     * @param networkCreatedAt The moment the blockchain recorded the transaction.
     * @param createdAt        The moment the merchant recorded the transaction.
     * @param updatedAt        The moment the merchant last changed the transaction.
     */
    @JsonCreator
    public UnconfirmedTransaction(@JsonProperty("id") final String id,
                                  @JsonProperty("tx_hash") final String txHash,
                                  @JsonProperty("bc_uniq_key") final String bcUniqKey,
                                  @JsonProperty("type") final String type,
                                  @JsonProperty("blockchain") final String blockchain,
                                  @JsonProperty("currency_id") final String currencyId,
                                  @JsonProperty("from_address") final String fromAddress,
                                  @JsonProperty("to_address") final String toAddress,
                                  @JsonProperty("amount") final String amount,
                                  @JsonProperty("amount_usd") final String amountUsd,
                                  @JsonProperty("network_created_at") final OffsetDateTime networkCreatedAt,
                                  @JsonProperty("created_at") final OffsetDateTime createdAt,
                                  @JsonProperty("updated_at") final OffsetDateTime updatedAt) {
        this.id = id;
        this.txHash = txHash;
        this.bcUniqKey = bcUniqKey;
        this.type = type;
        this.blockchain = blockchain;
        this.currencyId = currencyId;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.networkCreatedAt = networkCreatedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the transaction identifier in the merchant system.
     *
     * @return The transaction identifier in the merchant system.
     */
    public String getId() {
        return id;
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
     * Returns the unique event key within the transaction, used together with {@code txHash} to deduplicate events.
     *
     * @return The unique event key within the transaction, used together with {@code txHash} to deduplicate events.
     */
    public String getBcUniqKey() {
        return bcUniqKey;
    }

    /**
     * Returns the transaction type.
     *
     * @return The transaction type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the blockchain the transfer happened on.
     *
     * @return The blockchain the transfer happened on.
     */
    public String getBlockchain() {
        return blockchain;
    }

    /**
     * Returns the currency being transferred, for example {@code USDT.Tron}.
     *
     * @return The currency being transferred, for example {@code USDT.Tron}.
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Returns the sending address.
     *
     * @return The sending address.
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * Returns the receiving address, one of your deposit addresses.
     *
     * @return The receiving address, one of your deposit addresses.
     */
    public String getToAddress() {
        return toAddress;
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
     * Returns the same amount converted to USD, may be {@code null}.
     *
     * @return The same amount converted to USD, may be {@code null}.
     */
    public String getAmountUsd() {
        return amountUsd;
    }

    /**
     * Returns the moment the blockchain recorded the transaction.
     *
     * @return The moment the blockchain recorded the transaction.
     */
    public OffsetDateTime getNetworkCreatedAt() {
        return networkCreatedAt;
    }

    /**
     * Returns the moment the merchant recorded the transaction.
     *
     * @return The moment the merchant recorded the transaction.
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the moment the merchant last changed the transaction.
     *
     * @return The moment the merchant last changed the transaction.
     */
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UnconfirmedTransaction)) {
            return false;
        }

        UnconfirmedTransaction that = (UnconfirmedTransaction) other;

        return Objects.equals(id, that.id)
                && Objects.equals(txHash, that.txHash)
                && Objects.equals(bcUniqKey, that.bcUniqKey)
                && Objects.equals(type, that.type)
                && Objects.equals(blockchain, that.blockchain)
                && Objects.equals(currencyId, that.currencyId)
                && Objects.equals(fromAddress, that.fromAddress)
                && Objects.equals(toAddress, that.toAddress)
                && Objects.equals(amount, that.amount)
                && Objects.equals(amountUsd, that.amountUsd)
                && Objects.equals(networkCreatedAt, that.networkCreatedAt)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, txHash, bcUniqKey, type, blockchain, currencyId, fromAddress, toAddress, amount,
                            amountUsd, networkCreatedAt, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "UnconfirmedTransaction{"
                + "id=" + id
                + ", txHash=" + txHash
                + ", bcUniqKey=" + bcUniqKey
                + ", type=" + type
                + ", blockchain=" + blockchain
                + ", currencyId=" + currencyId
                + ", fromAddress=" + fromAddress
                + ", toAddress=" + toAddress
                + ", amount=" + amount
                + ", amountUsd=" + amountUsd
                + ", networkCreatedAt=" + networkCreatedAt
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
