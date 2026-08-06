package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Blockchain specific details of a processing wallet. Only chains with a resource model populate a
 * member here, so every field may be {@code null}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class BlockchainAdditionalData {

    @JsonProperty("tron_data")
    private final TronData tronData;

    /**
     * Constructs a {@code BlockchainAdditionalData}.
     *
     * @param tronData The Tron resources of the wallet, {@code null} outside Tron.
     */
    @JsonCreator
    public BlockchainAdditionalData(@JsonProperty("tron_data") final TronData tronData) {
        this.tronData = tronData;
    }

    /**
     * Returns the Tron resources of the wallet, {@code null} outside Tron.
     *
     * @return The Tron resources of the wallet, {@code null} outside Tron.
     */
    public TronData getTronData() {
        return tronData;
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

        return Objects.equals(tronData, that.tronData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tronData);
    }

    @Override
    public String toString() {
        return "BlockchainAdditionalData{"
                + "tronData=" + tronData
                + '}';
    }
}
