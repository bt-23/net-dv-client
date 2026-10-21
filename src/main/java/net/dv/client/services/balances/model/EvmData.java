package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The gas estimates of a processing wallet on an EVM compatible chain. Every amount is a decimal
 * string; the costs are expressed in Wei.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class EvmData {

    @JsonProperty("suggested_gas_price")
    private final String suggestedGasPrice;

    @JsonProperty("max_transfers_erc20")
    private final String maxTransfersErc20;

    @JsonProperty("max_transfers_native")
    private final String maxTransfersNative;

    @JsonProperty("cost_per_erc20")
    private final String costPerErc20;

    @JsonProperty("cost_per_native")
    private final String costPerNative;

    @JsonProperty("is_l2")
    private final Boolean l2;

    @JsonProperty("l1_data_fee_estimate")
    private final String l1DataFeeEstimate;

    /**
     * Constructs an {@code EvmData}.
     *
     * @param suggestedGasPrice  The gas price currently suggested by the node.
     * @param maxTransfersErc20  How many token transfers the native balance can pay for.
     * @param maxTransfersNative How many native coin transfers the native balance can pay for.
     * @param costPerErc20       The cost of a single token transfer, in Wei.
     * @param costPerNative      The cost of a single native coin transfer, in Wei.
     * @param l2                 Whether the chain is a layer 2 rollup.
     * @param l1DataFeeEstimate  The estimated layer 1 data fee, only meaningful on layer 2 chains.
     */
    @JsonCreator
    public EvmData(@JsonProperty("suggested_gas_price") final String suggestedGasPrice,
                   @JsonProperty("max_transfers_erc20") final String maxTransfersErc20,
                   @JsonProperty("max_transfers_native") final String maxTransfersNative,
                   @JsonProperty("cost_per_erc20") final String costPerErc20,
                   @JsonProperty("cost_per_native") final String costPerNative,
                   @JsonProperty("is_l2") final Boolean l2,
                   @JsonProperty("l1_data_fee_estimate") final String l1DataFeeEstimate) {
        this.suggestedGasPrice = suggestedGasPrice;
        this.maxTransfersErc20 = maxTransfersErc20;
        this.maxTransfersNative = maxTransfersNative;
        this.costPerErc20 = costPerErc20;
        this.costPerNative = costPerNative;
        this.l2 = l2;
        this.l1DataFeeEstimate = l1DataFeeEstimate;
    }

    /**
     * Returns the gas price currently suggested by the node.
     *
     * @return The suggested gas price.
     */
    public String getSuggestedGasPrice() {
        return suggestedGasPrice;
    }

    /**
     * Returns how many token transfers the native balance can pay for.
     *
     * @return The number of affordable token transfers.
     */
    public String getMaxTransfersErc20() {
        return maxTransfersErc20;
    }

    /**
     * Returns how many native coin transfers the native balance can pay for.
     *
     * @return The number of affordable native coin transfers.
     */
    public String getMaxTransfersNative() {
        return maxTransfersNative;
    }

    /**
     * Returns the cost of a single token transfer, in Wei.
     *
     * @return The cost of a single token transfer.
     */
    public String getCostPerErc20() {
        return costPerErc20;
    }

    /**
     * Returns the cost of a single native coin transfer, in Wei.
     *
     * @return The cost of a single native coin transfer.
     */
    public String getCostPerNative() {
        return costPerNative;
    }

    /**
     * Returns whether the chain is a layer 2 rollup.
     *
     * @return {@code true} on a layer 2 chain.
     */
    public Boolean getL2() {
        return l2;
    }

    /**
     * Returns the estimated layer 1 data fee, only meaningful on layer 2 chains.
     *
     * @return The estimated layer 1 data fee.
     */
    public String getL1DataFeeEstimate() {
        return l1DataFeeEstimate;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EvmData)) {
            return false;
        }

        EvmData that = (EvmData) other;

        return Objects.equals(suggestedGasPrice, that.suggestedGasPrice)
                && Objects.equals(maxTransfersErc20, that.maxTransfersErc20)
                && Objects.equals(maxTransfersNative, that.maxTransfersNative)
                && Objects.equals(costPerErc20, that.costPerErc20)
                && Objects.equals(costPerNative, that.costPerNative)
                && Objects.equals(l2, that.l2)
                && Objects.equals(l1DataFeeEstimate, that.l1DataFeeEstimate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suggestedGasPrice, maxTransfersErc20, maxTransfersNative, costPerErc20,
                costPerNative, l2, l1DataFeeEstimate);
    }

    @Override
    public String toString() {
        return "EvmData{"
                + "suggestedGasPrice=" + suggestedGasPrice
                + ", maxTransfersErc20=" + maxTransfersErc20
                + ", maxTransfersNative=" + maxTransfersNative
                + ", costPerErc20=" + costPerErc20
                + ", costPerNative=" + costPerNative
                + ", l2=" + l2
                + ", l1DataFeeEstimate=" + l1DataFeeEstimate
                + '}';
    }
}
