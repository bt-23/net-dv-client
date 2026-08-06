package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * A token balance held by a processing wallet.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Asset {

    @JsonProperty("identity")
    private final String identity;

    @JsonProperty("currency_id")
    private final String currencyId;

    @JsonProperty("amount")
    private final String amount;

    @JsonProperty("amount_usd")
    private final String amountUsd;

    /**
     * Constructs a {@code Asset}.
     *
     * @param identity   The token identity, usually its contract address or ticker.
     * @param currencyId The currency identifier the asset maps to, for example {@code USDT.Tron}.
     * @param amount     The balance in the token itself.
     * @param amountUsd  The same balance converted to USD.
     */
    @JsonCreator
    public Asset(@JsonProperty("identity") final String identity,
                 @JsonProperty("currency_id") final String currencyId,
                 @JsonProperty("amount") final String amount,
                 @JsonProperty("amount_usd") final String amountUsd) {
        this.identity = identity;
        this.currencyId = currencyId;
        this.amount = amount;
        this.amountUsd = amountUsd;
    }

    /**
     * Returns the token identity, usually its contract address or ticker.
     *
     * @return The token identity, usually its contract address or ticker.
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Returns the currency identifier the asset maps to, for example {@code USDT.Tron}.
     *
     * @return The currency identifier the asset maps to, for example {@code USDT.Tron}.
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Returns the balance in the token itself.
     *
     * @return The balance in the token itself.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Returns the same balance converted to USD.
     *
     * @return The same balance converted to USD.
     */
    public String getAmountUsd() {
        return amountUsd;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Asset)) {
            return false;
        }

        Asset that = (Asset) other;

        return Objects.equals(identity, that.identity)
                && Objects.equals(currencyId, that.currencyId)
                && Objects.equals(amount, that.amount)
                && Objects.equals(amountUsd, that.amountUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, currencyId, amount, amountUsd);
    }

    @Override
    public String toString() {
        return "Asset{"
                + "identity=" + identity
                + ", currencyId=" + currencyId
                + ", amount=" + amount
                + ", amountUsd=" + amountUsd
                + '}';
    }
}
