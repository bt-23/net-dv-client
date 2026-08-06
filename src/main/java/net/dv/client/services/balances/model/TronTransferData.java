package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * How many transfers the staked Tron resources still cover, which is the practical way to know
 * whether the processing wallet needs a top up.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TronTransferData {

    @JsonProperty("max_transfers_trc20")
    private final String maxTransfersTrc20;

    @JsonProperty("max_transfers_native")
    private final String maxTransfersNative;

    /**
     * Constructs a {@code TronTransferData}.
     *
     * @param maxTransfersTrc20  The number of TRC20 transfers the current resources cover.
     * @param maxTransfersNative The number of native TRX transfers the current resources cover.
     */
    @JsonCreator
    public TronTransferData(@JsonProperty("max_transfers_trc20") final String maxTransfersTrc20,
                            @JsonProperty("max_transfers_native") final String maxTransfersNative) {
        this.maxTransfersTrc20 = maxTransfersTrc20;
        this.maxTransfersNative = maxTransfersNative;
    }

    /**
     * Returns the number of TRC20 transfers the current resources cover.
     *
     * @return The number of TRC20 transfers the current resources cover.
     */
    public String getMaxTransfersTrc20() {
        return maxTransfersTrc20;
    }

    /**
     * Returns the number of native TRX transfers the current resources cover.
     *
     * @return The number of native TRX transfers the current resources cover.
     */
    public String getMaxTransfersNative() {
        return maxTransfersNative;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TronTransferData)) {
            return false;
        }

        TronTransferData that = (TronTransferData) other;

        return Objects.equals(maxTransfersTrc20, that.maxTransfersTrc20)
                && Objects.equals(maxTransfersNative, that.maxTransfersNative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxTransfersTrc20, maxTransfersNative);
    }

    @Override
    public String toString() {
        return "TronTransferData{"
                + "maxTransfersTrc20=" + maxTransfersTrc20
                + ", maxTransfersNative=" + maxTransfersNative
                + '}';
    }
}
