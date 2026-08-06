package net.dv.client.services.withdrawals.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The current state of a withdrawal, including the blockchain transaction once it is broadcast.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WithdrawalStatus {

    @JsonProperty("transfer")
    private final Transfer transfer;

    @JsonProperty("tx_hash")
    private final String txHash;

    @JsonProperty("store_id")
    private final String storeId;

    @JsonProperty("currency_id")
    private final String currencyId;

    @JsonProperty("address_from")
    private final String addressFrom;

    @JsonProperty("address_to")
    private final String addressTo;

    @JsonProperty("amount")
    private final String amount;

    @JsonProperty("amount_usd")
    private final String amountUsd;

    @JsonProperty("created_at")
    private final OffsetDateTime createdAt;

    /**
     * Constructs a {@code WithdrawalStatus}.
     *
     * @param transfer    The progress of the underlying transfer, {@code null} before it starts.
     * @param txHash      The blockchain transaction hash, empty until the transfer is broadcast.
     * @param storeId     The identifier of the store the withdrawal belongs to.
     * @param currencyId  The currency being withdrawn, for example {@code USDT.Tron}.
     * @param addressFrom The processing wallet address the funds leave from.
     * @param addressTo   The address the funds are sent to.
     * @param amount      The amount in the currency itself.
     * @param amountUsd   The same amount converted to USD.
     * @param createdAt   The moment the withdrawal was accepted.
     */
    @JsonCreator
    public WithdrawalStatus(@JsonProperty("transfer") final Transfer transfer,
                            @JsonProperty("tx_hash") final String txHash,
                            @JsonProperty("store_id") final String storeId,
                            @JsonProperty("currency_id") final String currencyId,
                            @JsonProperty("address_from") final String addressFrom,
                            @JsonProperty("address_to") final String addressTo,
                            @JsonProperty("amount") final String amount,
                            @JsonProperty("amount_usd") final String amountUsd,
                            @JsonProperty("created_at") final OffsetDateTime createdAt) {
        this.transfer = transfer;
        this.txHash = txHash;
        this.storeId = storeId;
        this.currencyId = currencyId;
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.createdAt = createdAt;
    }

    /**
     * Returns the progress of the underlying transfer, {@code null} before it starts.
     *
     * @return The progress of the underlying transfer, {@code null} before it starts.
     */
    public Transfer getTransfer() {
        return transfer;
    }

    /**
     * Returns the blockchain transaction hash, empty until the transfer is broadcast.
     *
     * @return The blockchain transaction hash, empty until the transfer is broadcast.
     */
    public String getTxHash() {
        return txHash;
    }

    /**
     * Returns the identifier of the store the withdrawal belongs to.
     *
     * @return The identifier of the store the withdrawal belongs to.
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * Returns the currency being withdrawn, for example {@code USDT.Tron}.
     *
     * @return The currency being withdrawn, for example {@code USDT.Tron}.
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Returns the processing wallet address the funds leave from.
     *
     * @return The processing wallet address the funds leave from.
     */
    public String getAddressFrom() {
        return addressFrom;
    }

    /**
     * Returns the address the funds are sent to.
     *
     * @return The address the funds are sent to.
     */
    public String getAddressTo() {
        return addressTo;
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
     * Returns the moment the withdrawal was accepted.
     *
     * @return The moment the withdrawal was accepted.
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WithdrawalStatus)) {
            return false;
        }

        WithdrawalStatus that = (WithdrawalStatus) other;

        return Objects.equals(transfer, that.transfer)
                && Objects.equals(txHash, that.txHash)
                && Objects.equals(storeId, that.storeId)
                && Objects.equals(currencyId, that.currencyId)
                && Objects.equals(addressFrom, that.addressFrom)
                && Objects.equals(addressTo, that.addressTo)
                && Objects.equals(amount, that.amount)
                && Objects.equals(amountUsd, that.amountUsd)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transfer, txHash, storeId, currencyId, addressFrom, addressTo, amount, amountUsd,
                            createdAt);
    }

    @Override
    public String toString() {
        return "WithdrawalStatus{"
                + "transfer=" + transfer
                + ", txHash=" + txHash
                + ", storeId=" + storeId
                + ", currencyId=" + currencyId
                + ", addressFrom=" + addressFrom
                + ", addressTo=" + addressTo
                + ", amount=" + amount
                + ", amountUsd=" + amountUsd
                + ", createdAt=" + createdAt
                + '}';
    }
}
