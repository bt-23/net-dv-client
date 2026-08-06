package net.dv.client.services.wallets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv.client.services.common.model.CurrencyShort;

import java.util.Objects;

/**
 * The aggregated hot wallet balance of the store for a single currency.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WalletSummary {

    @JsonProperty("currency")
    private final CurrencyShort currency;

    @JsonProperty("balance")
    private final String balance;

    @JsonProperty("balance_usd")
    private final String balanceUsd;

    @JsonProperty("count")
    private final Integer count;

    @JsonProperty("count_with_balance")
    private final Integer countWithBalance;

    /**
     * Constructs a {@code WalletSummary}.
     *
     * @param currency         The currency the totals are expressed in.
     * @param balance          The total balance in the currency itself.
     * @param balanceUsd       The total balance converted to USD.
     * @param count            The number of hot wallet addresses in this currency.
     * @param countWithBalance The number of those addresses that currently hold funds.
     */
    @JsonCreator
    public WalletSummary(@JsonProperty("currency") final CurrencyShort currency,
                         @JsonProperty("balance") final String balance,
                         @JsonProperty("balance_usd") final String balanceUsd,
                         @JsonProperty("count") final Integer count,
                         @JsonProperty("count_with_balance") final Integer countWithBalance) {
        this.currency = currency;
        this.balance = balance;
        this.balanceUsd = balanceUsd;
        this.count = count;
        this.countWithBalance = countWithBalance;
    }

    /**
     * Returns the currency the totals are expressed in.
     *
     * @return The currency the totals are expressed in.
     */
    public CurrencyShort getCurrency() {
        return currency;
    }

    /**
     * Returns the total balance in the currency itself.
     *
     * @return The total balance in the currency itself.
     */
    public String getBalance() {
        return balance;
    }

    /**
     * Returns the total balance converted to USD.
     *
     * @return The total balance converted to USD.
     */
    public String getBalanceUsd() {
        return balanceUsd;
    }

    /**
     * Returns the number of hot wallet addresses in this currency.
     *
     * @return The number of hot wallet addresses in this currency.
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Returns the number of those addresses that currently hold funds.
     *
     * @return The number of those addresses that currently hold funds.
     */
    public Integer getCountWithBalance() {
        return countWithBalance;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WalletSummary)) {
            return false;
        }

        WalletSummary that = (WalletSummary) other;

        return Objects.equals(currency, that.currency)
                && Objects.equals(balance, that.balance)
                && Objects.equals(balanceUsd, that.balanceUsd)
                && Objects.equals(count, that.count)
                && Objects.equals(countWithBalance, that.countWithBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, balance, balanceUsd, count, countWithBalance);
    }

    @Override
    public String toString() {
        return "WalletSummary{"
                + "currency=" + currency
                + ", balance=" + balance
                + ", balanceUsd=" + balanceUsd
                + ", count=" + count
                + ", countWithBalance=" + countWithBalance
                + '}';
    }
}
