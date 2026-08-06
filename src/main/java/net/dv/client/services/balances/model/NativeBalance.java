package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The native coin balance of a processing wallet, which is what pays the network fees.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class NativeBalance {

    @JsonProperty("native_token")
    private final String nativeToken;

    @JsonProperty("native_token_usd")
    private final String nativeTokenUsd;

    /**
     * Constructs a {@code NativeBalance}.
     *
     * @param nativeToken    The balance in the native coin, for example TRX on Tron.
     * @param nativeTokenUsd The same balance converted to USD.
     */
    @JsonCreator
    public NativeBalance(@JsonProperty("native_token") final String nativeToken,
                         @JsonProperty("native_token_usd") final String nativeTokenUsd) {
        this.nativeToken = nativeToken;
        this.nativeTokenUsd = nativeTokenUsd;
    }

    /**
     * Returns the balance in the native coin, for example TRX on Tron.
     *
     * @return The balance in the native coin, for example TRX on Tron.
     */
    public String getNativeToken() {
        return nativeToken;
    }

    /**
     * Returns the same balance converted to USD.
     *
     * @return The same balance converted to USD.
     */
    public String getNativeTokenUsd() {
        return nativeTokenUsd;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NativeBalance)) {
            return false;
        }

        NativeBalance that = (NativeBalance) other;

        return Objects.equals(nativeToken, that.nativeToken)
                && Objects.equals(nativeTokenUsd, that.nativeTokenUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nativeToken, nativeTokenUsd);
    }

    @Override
    public String toString() {
        return "NativeBalance{"
                + "nativeToken=" + nativeToken
                + ", nativeTokenUsd=" + nativeTokenUsd
                + '}';
    }
}
