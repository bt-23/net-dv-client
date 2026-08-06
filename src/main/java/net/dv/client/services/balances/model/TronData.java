package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The Tron specific resources of a processing wallet: staked TRX and the bandwidth and energy it
 * currently buys.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TronData {

    @JsonProperty("tron_transfer_data")
    private final TronTransferData tronTransferData;

    @JsonProperty("stacked_trx")
    private final String stackedTrx;

    @JsonProperty("stacked_energy")
    private final String stackedEnergy;

    @JsonProperty("stacked_energy_trx")
    private final String stackedEnergyTrx;

    @JsonProperty("stacked_bandwidth")
    private final String stackedBandwidth;

    @JsonProperty("stacked_bandwidth_trx")
    private final String stackedBandwidthTrx;

    @JsonProperty("total_energy")
    private final String totalEnergy;

    @JsonProperty("total_used_energy")
    private final String totalUsedEnergy;

    @JsonProperty("available_energy_for_use")
    private final String availableEnergyForUse;

    @JsonProperty("total_bandwidth")
    private final String totalBandwidth;

    @JsonProperty("total_used_bandwidth")
    private final String totalUsedBandwidth;

    @JsonProperty("available_bandwidth_for_use")
    private final String availableBandwidthForUse;

    /**
     * Constructs a {@code TronData}.
     *
     * @param tronTransferData         The number of transfers the current resources still cover.
     * @param stackedTrx               The amount of TRX staked in total.
     * @param stackedEnergy            The energy obtained from staking.
     * @param stackedEnergyTrx         The amount of TRX staked for energy.
     * @param stackedBandwidth         The bandwidth obtained from staking.
     * @param stackedBandwidthTrx      The amount of TRX staked for bandwidth.
     * @param totalEnergy              The total energy available to the wallet.
     * @param totalUsedEnergy          The energy already consumed.
     * @param availableEnergyForUse    The energy still available.
     * @param totalBandwidth           The total bandwidth available to the wallet.
     * @param totalUsedBandwidth       The bandwidth already consumed.
     * @param availableBandwidthForUse The bandwidth still available.
     */
    @JsonCreator
    public TronData(@JsonProperty("tron_transfer_data") final TronTransferData tronTransferData,
                    @JsonProperty("stacked_trx") final String stackedTrx,
                    @JsonProperty("stacked_energy") final String stackedEnergy,
                    @JsonProperty("stacked_energy_trx") final String stackedEnergyTrx,
                    @JsonProperty("stacked_bandwidth") final String stackedBandwidth,
                    @JsonProperty("stacked_bandwidth_trx") final String stackedBandwidthTrx,
                    @JsonProperty("total_energy") final String totalEnergy,
                    @JsonProperty("total_used_energy") final String totalUsedEnergy,
                    @JsonProperty("available_energy_for_use") final String availableEnergyForUse,
                    @JsonProperty("total_bandwidth") final String totalBandwidth,
                    @JsonProperty("total_used_bandwidth") final String totalUsedBandwidth,
                    @JsonProperty("available_bandwidth_for_use") final String availableBandwidthForUse) {
        this.tronTransferData = tronTransferData;
        this.stackedTrx = stackedTrx;
        this.stackedEnergy = stackedEnergy;
        this.stackedEnergyTrx = stackedEnergyTrx;
        this.stackedBandwidth = stackedBandwidth;
        this.stackedBandwidthTrx = stackedBandwidthTrx;
        this.totalEnergy = totalEnergy;
        this.totalUsedEnergy = totalUsedEnergy;
        this.availableEnergyForUse = availableEnergyForUse;
        this.totalBandwidth = totalBandwidth;
        this.totalUsedBandwidth = totalUsedBandwidth;
        this.availableBandwidthForUse = availableBandwidthForUse;
    }

    /**
     * Returns the number of transfers the current resources still cover.
     *
     * @return The number of transfers the current resources still cover.
     */
    public TronTransferData getTronTransferData() {
        return tronTransferData;
    }

    /**
     * Returns the amount of TRX staked in total.
     *
     * @return The amount of TRX staked in total.
     */
    public String getStackedTrx() {
        return stackedTrx;
    }

    /**
     * Returns the energy obtained from staking.
     *
     * @return The energy obtained from staking.
     */
    public String getStackedEnergy() {
        return stackedEnergy;
    }

    /**
     * Returns the amount of TRX staked for energy.
     *
     * @return The amount of TRX staked for energy.
     */
    public String getStackedEnergyTrx() {
        return stackedEnergyTrx;
    }

    /**
     * Returns the bandwidth obtained from staking.
     *
     * @return The bandwidth obtained from staking.
     */
    public String getStackedBandwidth() {
        return stackedBandwidth;
    }

    /**
     * Returns the amount of TRX staked for bandwidth.
     *
     * @return The amount of TRX staked for bandwidth.
     */
    public String getStackedBandwidthTrx() {
        return stackedBandwidthTrx;
    }

    /**
     * Returns the total energy available to the wallet.
     *
     * @return The total energy available to the wallet.
     */
    public String getTotalEnergy() {
        return totalEnergy;
    }

    /**
     * Returns the energy already consumed.
     *
     * @return The energy already consumed.
     */
    public String getTotalUsedEnergy() {
        return totalUsedEnergy;
    }

    /**
     * Returns the energy still available.
     *
     * @return The energy still available.
     */
    public String getAvailableEnergyForUse() {
        return availableEnergyForUse;
    }

    /**
     * Returns the total bandwidth available to the wallet.
     *
     * @return The total bandwidth available to the wallet.
     */
    public String getTotalBandwidth() {
        return totalBandwidth;
    }

    /**
     * Returns the bandwidth already consumed.
     *
     * @return The bandwidth already consumed.
     */
    public String getTotalUsedBandwidth() {
        return totalUsedBandwidth;
    }

    /**
     * Returns the bandwidth still available.
     *
     * @return The bandwidth still available.
     */
    public String getAvailableBandwidthForUse() {
        return availableBandwidthForUse;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TronData)) {
            return false;
        }

        TronData that = (TronData) other;

        return Objects.equals(tronTransferData, that.tronTransferData)
                && Objects.equals(stackedTrx, that.stackedTrx)
                && Objects.equals(stackedEnergy, that.stackedEnergy)
                && Objects.equals(stackedEnergyTrx, that.stackedEnergyTrx)
                && Objects.equals(stackedBandwidth, that.stackedBandwidth)
                && Objects.equals(stackedBandwidthTrx, that.stackedBandwidthTrx)
                && Objects.equals(totalEnergy, that.totalEnergy)
                && Objects.equals(totalUsedEnergy, that.totalUsedEnergy)
                && Objects.equals(availableEnergyForUse, that.availableEnergyForUse)
                && Objects.equals(totalBandwidth, that.totalBandwidth)
                && Objects.equals(totalUsedBandwidth, that.totalUsedBandwidth)
                && Objects.equals(availableBandwidthForUse, that.availableBandwidthForUse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tronTransferData, stackedTrx, stackedEnergy, stackedEnergyTrx, stackedBandwidth,
                            stackedBandwidthTrx, totalEnergy, totalUsedEnergy, availableEnergyForUse, totalBandwidth,
                            totalUsedBandwidth, availableBandwidthForUse);
    }

    @Override
    public String toString() {
        return "TronData{"
                + "tronTransferData=" + tronTransferData
                + ", stackedTrx=" + stackedTrx
                + ", stackedEnergy=" + stackedEnergy
                + ", stackedEnergyTrx=" + stackedEnergyTrx
                + ", stackedBandwidth=" + stackedBandwidth
                + ", stackedBandwidthTrx=" + stackedBandwidthTrx
                + ", totalEnergy=" + totalEnergy
                + ", totalUsedEnergy=" + totalUsedEnergy
                + ", availableEnergyForUse=" + availableEnergyForUse
                + ", totalBandwidth=" + totalBandwidth
                + ", totalUsedBandwidth=" + totalUsedBandwidth
                + ", availableBandwidthForUse=" + availableBandwidthForUse
                + '}';
    }
}
