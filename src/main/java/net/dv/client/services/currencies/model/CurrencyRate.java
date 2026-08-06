package net.dv.client.services.currencies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The exchange rate of a currency against the store settlement currency.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CurrencyRate {

    @JsonProperty("code")
    private final String code;

    @JsonProperty("rate")
    private final String rate;

    @JsonProperty("rate_source")
    private final String rateSource;

    /**
     * Constructs a {@code CurrencyRate}.
     *
     * @param code       The currency ticker the rate applies to.
     * @param rate       The rate, as a decimal string to preserve precision.
     * @param rateSource The provider the rate was taken from.
     */
    @JsonCreator
    public CurrencyRate(@JsonProperty("code") final String code,
                        @JsonProperty("rate") final String rate,
                        @JsonProperty("rate_source") final String rateSource) {
        this.code = code;
        this.rate = rate;
        this.rateSource = rateSource;
    }

    /**
     * Returns the currency ticker the rate applies to.
     *
     * @return The currency ticker the rate applies to.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the rate, as a decimal string to preserve precision.
     *
     * @return The rate, as a decimal string to preserve precision.
     */
    public String getRate() {
        return rate;
    }

    /**
     * Returns the provider the rate was taken from.
     *
     * @return The provider the rate was taken from.
     */
    public String getRateSource() {
        return rateSource;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CurrencyRate)) {
            return false;
        }

        CurrencyRate that = (CurrencyRate) other;

        return Objects.equals(code, that.code)
                && Objects.equals(rate, that.rate)
                && Objects.equals(rateSource, that.rateSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, rate, rateSource);
    }

    @Override
    public String toString() {
        return "CurrencyRate{"
                + "code=" + code
                + ", rate=" + rate
                + ", rateSource=" + rateSource
                + '}';
    }
}
