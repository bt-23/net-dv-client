package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The balance held on a connected exchange for a single currency.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ExchangeBalance {

    @JsonProperty("currency")
    private final String currency;

    @JsonProperty("amount")
    private final String amount;

    @JsonProperty("amount_usd")
    private final String amountUsd;

    /**
     * Constructs a {@code ExchangeBalance}.
     *
     * @param currency  The currency ticker.
     * @param amount    The balance in the currency itself.
     * @param amountUsd The balance converted to USD.
     */
    @JsonCreator
    public ExchangeBalance(@JsonProperty("currency") final String currency,
                           @JsonProperty("amount") final String amount,
                           @JsonProperty("amount_usd") final String amountUsd) {
        this.currency = currency;
        this.amount = amount;
        this.amountUsd = amountUsd;
    }

    /**
     * Returns the currency ticker.
     *
     * @return The currency ticker.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the balance in the currency itself.
     *
     * @return The balance in the currency itself.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Returns the balance converted to USD.
     *
     * @return The balance converted to USD.
     */
    public String getAmountUsd() {
        return amountUsd;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExchangeBalance)) {
            return false;
        }

        ExchangeBalance that = (ExchangeBalance) other;

        return Objects.equals(currency, that.currency)
                && Objects.equals(amount, that.amount)
                && Objects.equals(amountUsd, that.amountUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount, amountUsd);
    }

    @Override
    public String toString() {
        return "ExchangeBalance{"
                + "currency=" + currency
                + ", amount=" + amount
                + ", amountUsd=" + amountUsd
                + '}';
    }
}
