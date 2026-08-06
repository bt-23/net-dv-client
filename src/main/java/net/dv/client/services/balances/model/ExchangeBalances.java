package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * The balances held across every exchange connected to the merchant.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ExchangeBalances {

    @JsonProperty("total_usd")
    private final String totalUsd;

    @JsonProperty("balances")
    private final List<ExchangeBalance> balances;

    /**
     * Constructs a {@code ExchangeBalances}.
     *
     * @param totalUsd The sum of every balance, converted to USD.
     * @param balances The balance of each currency.
     */
    @JsonCreator
    public ExchangeBalances(@JsonProperty("total_usd") final String totalUsd,
                            @JsonProperty("balances") final List<ExchangeBalance> balances) {
        this.totalUsd = totalUsd;
        this.balances = balances;
    }

    /**
     * Returns the sum of every balance, converted to USD.
     *
     * @return The sum of every balance, converted to USD.
     */
    public String getTotalUsd() {
        return totalUsd;
    }

    /**
     * Returns the balance of each currency.
     *
     * @return The balance of each currency.
     */
    public List<ExchangeBalance> getBalances() {
        return balances;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExchangeBalances)) {
            return false;
        }

        ExchangeBalances that = (ExchangeBalances) other;

        return Objects.equals(totalUsd, that.totalUsd)
                && Objects.equals(balances, that.balances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalUsd, balances);
    }

    @Override
    public String toString() {
        return "ExchangeBalances{"
                + "totalUsd=" + totalUsd
                + ", balances=" + balances
                + '}';
    }
}
