package net.dv.client.services.currencies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The USD exchange rate of a currency ticker, as used by the store to price an invoice.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CurrencyRate {

    @JsonProperty("code")
    private final String code;

    @JsonProperty("rate")
    private final String rate;

    @JsonProperty("original_rate")
    private final String originalRate;

    @JsonProperty("rate_source")
    private final String rateSource;

    /**
     * Constructs a {@code CurrencyRate}.
     *
     * @param code         The currency ticker the rate applies to, for example {@code USDT}.
     * @param rate         The rate after the store rate scale is applied, as a decimal string.
     * @param originalRate The rate reported by the source before the store rate scale, as a decimal string.
     * @param rateSource   The provider the rate was taken from.
     */
    @JsonCreator
    public CurrencyRate(@JsonProperty("code") final String code,
                        @JsonProperty("rate") final String rate,
                        @JsonProperty("original_rate") final String originalRate,
                        @JsonProperty("rate_source") final String rateSource) {
        this.code = code;
        this.rate = rate;
        this.originalRate = originalRate;
        this.rateSource = rateSource;
    }

    /**
     * Returns the currency ticker the rate applies to, for example {@code USDT}.
     *
     * @return The currency ticker the rate applies to.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the rate after the store rate scale is applied, as a decimal string.
     *
     * @return The rate the store prices invoices with.
     */
    public String getRate() {
        return rate;
    }

    /**
     * Returns the rate reported by the source before the store rate scale, as a decimal string.
     *
     * @return The unscaled source rate.
     */
    public String getOriginalRate() {
        return originalRate;
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
                && Objects.equals(originalRate, that.originalRate)
                && Objects.equals(rateSource, that.rateSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, rate, originalRate, rateSource);
    }

    @Override
    public String toString() {
        return "CurrencyRate{"
                + "code=" + code
                + ", rate=" + rate
                + ", originalRate=" + originalRate
                + ", rateSource=" + rateSource
                + '}';
    }
}
