package net.dv.client.services.balances.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv.client.services.common.model.CurrencyShort;

import java.util.List;
import java.util.Objects;

/**
 * The balance of a processing wallet, the wallet the merchant pays withdrawals and network fees
 * from.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ProcessingWalletBalance {

    @JsonProperty("address")
    private final String address;

    @JsonProperty("blockchain")
    private final String blockchain;

    @JsonProperty("currency")
    private final CurrencyShort currency;

    @JsonProperty("balance")
    private final NativeBalance balance;

    @JsonProperty("assets")
    private final List<Asset> assets;

    @JsonProperty("additional_data")
    private final BlockchainAdditionalData additionalData;

    /**
     * Constructs a {@code ProcessingWalletBalance}.
     *
     * @param address        The blockchain address of the processing wallet.
     * @param blockchain     The blockchain the wallet lives on.
     * @param currency       The native currency of the blockchain.
     * @param balance        The native coin balance, which funds the network fees.
     * @param assets         The token balances held by the wallet.
     * @param additionalData The blockchain specific resources of the wallet, may be {@code null}.
     */
    @JsonCreator
    public ProcessingWalletBalance(@JsonProperty("address") final String address,
                                   @JsonProperty("blockchain") final String blockchain,
                                   @JsonProperty("currency") final CurrencyShort currency,
                                   @JsonProperty("balance") final NativeBalance balance,
                                   @JsonProperty("assets") final List<Asset> assets,
                                   @JsonProperty("additional_data") final BlockchainAdditionalData additionalData) {
        this.address = address;
        this.blockchain = blockchain;
        this.currency = currency;
        this.balance = balance;
        this.assets = assets;
        this.additionalData = additionalData;
    }

    /**
     * Returns the blockchain address of the processing wallet.
     *
     * @return The blockchain address of the processing wallet.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the blockchain the wallet lives on.
     *
     * @return The blockchain the wallet lives on.
     */
    public String getBlockchain() {
        return blockchain;
    }

    /**
     * Returns the native currency of the blockchain.
     *
     * @return The native currency of the blockchain.
     */
    public CurrencyShort getCurrency() {
        return currency;
    }

    /**
     * Returns the native coin balance, which funds the network fees.
     *
     * @return The native coin balance, which funds the network fees.
     */
    public NativeBalance getBalance() {
        return balance;
    }

    /**
     * Returns the token balances held by the wallet.
     *
     * @return The token balances held by the wallet.
     */
    public List<Asset> getAssets() {
        return assets;
    }

    /**
     * Returns the blockchain specific resources of the wallet, may be {@code null}.
     *
     * @return The blockchain specific resources of the wallet, may be {@code null}.
     */
    public BlockchainAdditionalData getAdditionalData() {
        return additionalData;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProcessingWalletBalance)) {
            return false;
        }

        ProcessingWalletBalance that = (ProcessingWalletBalance) other;

        return Objects.equals(address, that.address)
                && Objects.equals(blockchain, that.blockchain)
                && Objects.equals(currency, that.currency)
                && Objects.equals(balance, that.balance)
                && Objects.equals(assets, that.assets)
                && Objects.equals(additionalData, that.additionalData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, blockchain, currency, balance, assets, additionalData);
    }

    @Override
    public String toString() {
        return "ProcessingWalletBalance{"
                + "address=" + address
                + ", blockchain=" + blockchain
                + ", currency=" + currency
                + ", balance=" + balance
                + ", assets=" + assets
                + ", additionalData=" + additionalData
                + '}';
    }
}
