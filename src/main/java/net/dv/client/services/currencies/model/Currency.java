package net.dv.client.services.currencies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv.client.services.common.model.Icon;

import java.util.Objects;

/**
 * A currency enabled on the store.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Currency {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("code")
    private final String code;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("precision")
    private final Integer precision;

    @JsonProperty("is_fiat")
    private final Boolean isFiat;

    @JsonProperty("blockchain")
    private final String blockchain;

    @JsonProperty("contract_address")
    private final String contractAddress;

    @JsonProperty("withdrawal_min_balance")
    private final String withdrawalMinBalance;

    @JsonProperty("has_balance")
    private final Boolean hasBalance;

    @JsonProperty("status")
    private final Boolean status;

    @JsonProperty("min_confirmation")
    private final Integer minConfirmation;

    @JsonProperty("icon")
    private final Icon icon;

    @JsonProperty("blockchain_icon")
    private final Icon blockchainIcon;

    @JsonProperty("explorer_link")
    private final String explorerLink;

    /**
     * Constructs a {@code Currency}.
     *
     * @param id                   The currency identifier, for example {@code USDT.Tron}.
     * @param code                 The currency ticker, for example {@code USDT}.
     * @param name                 The display name of the currency.
     * @param precision            The number of decimals the currency supports.
     * @param isFiat               Whether the currency is a fiat currency.
     * @param blockchain           The blockchain the currency lives on.
     * @param contractAddress      The token contract address, empty for native coins.
     * @param withdrawalMinBalance The smallest balance that can be withdrawn.
     * @param hasBalance           Whether the store currently holds a balance in this currency.
     * @param status               Whether the currency is enabled for payments.
     * @param minConfirmation      The number of blockchain confirmations required to credit a payment.
     * @param icon                 The currency artwork.
     * @param blockchainIcon       The blockchain artwork.
     * @param explorerLink         The block explorer URL template for this currency.
     */
    @JsonCreator
    public Currency(@JsonProperty("id") final String id,
                    @JsonProperty("code") final String code,
                    @JsonProperty("name") final String name,
                    @JsonProperty("precision") final Integer precision,
                    @JsonProperty("is_fiat") final Boolean isFiat,
                    @JsonProperty("blockchain") final String blockchain,
                    @JsonProperty("contract_address") final String contractAddress,
                    @JsonProperty("withdrawal_min_balance") final String withdrawalMinBalance,
                    @JsonProperty("has_balance") final Boolean hasBalance,
                    @JsonProperty("status") final Boolean status,
                    @JsonProperty("min_confirmation") final Integer minConfirmation,
                    @JsonProperty("icon") final Icon icon,
                    @JsonProperty("blockchain_icon") final Icon blockchainIcon,
                    @JsonProperty("explorer_link") final String explorerLink) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.precision = precision;
        this.isFiat = isFiat;
        this.blockchain = blockchain;
        this.contractAddress = contractAddress;
        this.withdrawalMinBalance = withdrawalMinBalance;
        this.hasBalance = hasBalance;
        this.status = status;
        this.minConfirmation = minConfirmation;
        this.icon = icon;
        this.blockchainIcon = blockchainIcon;
        this.explorerLink = explorerLink;
    }

    /**
     * Returns the currency identifier, for example {@code USDT.Tron}.
     *
     * @return The currency identifier, for example {@code USDT.Tron}.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the currency ticker, for example {@code USDT}.
     *
     * @return The currency ticker, for example {@code USDT}.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the display name of the currency.
     *
     * @return The display name of the currency.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of decimals the currency supports.
     *
     * @return The number of decimals the currency supports.
     */
    public Integer getPrecision() {
        return precision;
    }

    /**
     * Returns whether the currency is a fiat currency.
     *
     * @return Whether the currency is a fiat currency.
     */
    public Boolean getIsFiat() {
        return isFiat;
    }

    /**
     * Returns the blockchain the currency lives on.
     *
     * @return The blockchain the currency lives on.
     */
    public String getBlockchain() {
        return blockchain;
    }

    /**
     * Returns the token contract address, empty for native coins.
     *
     * @return The token contract address, empty for native coins.
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Returns the smallest balance that can be withdrawn.
     *
     * @return The smallest balance that can be withdrawn.
     */
    public String getWithdrawalMinBalance() {
        return withdrawalMinBalance;
    }

    /**
     * Returns whether the store currently holds a balance in this currency.
     *
     * @return Whether the store currently holds a balance in this currency.
     */
    public Boolean getHasBalance() {
        return hasBalance;
    }

    /**
     * Returns whether the currency is enabled for payments.
     *
     * @return Whether the currency is enabled for payments.
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * Returns the number of blockchain confirmations required to credit a payment.
     *
     * @return The number of blockchain confirmations required to credit a payment.
     */
    public Integer getMinConfirmation() {
        return minConfirmation;
    }

    /**
     * Returns the currency artwork.
     *
     * @return The currency artwork.
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Returns the blockchain artwork.
     *
     * @return The blockchain artwork.
     */
    public Icon getBlockchainIcon() {
        return blockchainIcon;
    }

    /**
     * Returns the block explorer URL template for this currency.
     *
     * @return The block explorer URL template for this currency.
     */
    public String getExplorerLink() {
        return explorerLink;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Currency)) {
            return false;
        }

        Currency that = (Currency) other;

        return Objects.equals(id, that.id)
                && Objects.equals(code, that.code)
                && Objects.equals(name, that.name)
                && Objects.equals(precision, that.precision)
                && Objects.equals(isFiat, that.isFiat)
                && Objects.equals(blockchain, that.blockchain)
                && Objects.equals(contractAddress, that.contractAddress)
                && Objects.equals(withdrawalMinBalance, that.withdrawalMinBalance)
                && Objects.equals(hasBalance, that.hasBalance)
                && Objects.equals(status, that.status)
                && Objects.equals(minConfirmation, that.minConfirmation)
                && Objects.equals(icon, that.icon)
                && Objects.equals(blockchainIcon, that.blockchainIcon)
                && Objects.equals(explorerLink, that.explorerLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, precision, isFiat, blockchain, contractAddress, withdrawalMinBalance,
                            hasBalance, status, minConfirmation, icon, blockchainIcon, explorerLink);
    }

    @Override
    public String toString() {
        return "Currency{"
                + "id=" + id
                + ", code=" + code
                + ", name=" + name
                + ", precision=" + precision
                + ", isFiat=" + isFiat
                + ", blockchain=" + blockchain
                + ", contractAddress=" + contractAddress
                + ", withdrawalMinBalance=" + withdrawalMinBalance
                + ", hasBalance=" + hasBalance
                + ", status=" + status
                + ", minConfirmation=" + minConfirmation
                + ", icon=" + icon
                + ", blockchainIcon=" + blockchainIcon
                + ", explorerLink=" + explorerLink
                + '}';
    }
}
