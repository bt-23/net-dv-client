package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Blockchain specific details of a processing wallet. At most one member is populated, matching
 * the chain family of the wallet, so every field may be {@code null}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class BlockchainAdditionalData {

    @JsonProperty("tron_data")
    private final TronData tronData;

    @JsonProperty("evm_data")
    private final EvmData evmData;

    /**
     * Constructs a {@code BlockchainAdditionalData}.
     *
     * @param tronData The Tron resources of the wallet, {@code null} outside Tron.
     * @param evmData  The gas estimates of the wallet, {@code null} outside EVM chains.
     */
    @JsonCreator
    public BlockchainAdditionalData(@JsonProperty("tron_data") final TronData tronData,
                                    @JsonProperty("evm_data") final EvmData evmData) {
        this.tronData = tronData;
        this.evmData = evmData;
    }

    /**
     * Returns the Tron resources of the wallet, {@code null} outside Tron.
     *
     * @return The Tron resources of the wallet, {@code null} outside Tron.
     */
    public TronData getTronData() {
        return tronData;
    }

    /**
     * Returns the gas estimates of the wallet, {@code null} outside EVM chains.
     *
     * @return The gas estimates of the wallet, {@code null} outside EVM chains.
     */
    public EvmData getEvmData() {
        return evmData;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BlockchainAdditionalData)) {
            return false;
        }

        BlockchainAdditionalData that = (BlockchainAdditionalData) other;

        return Objects.equals(tronData, that.tronData)
                && Objects.equals(evmData, that.evmData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tronData, evmData);
    }

    @Override
    public String toString() {
        return "BlockchainAdditionalData{"
                + "tronData=" + tronData
                + ", evmData=" + evmData
                + '}';
    }
}
