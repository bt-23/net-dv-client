package net.dv.client.services.currencies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv.client.services.common.model.Icon;

import java.util.Objects;

/**
 * A currency as returned by the extended catalogue, which additionally carries the artwork of the
 * token itself so that a payment form can render token and blockchain separately.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ExtendedCurrency {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("code")
    private final String code;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("blockchain")
    private final String blockchain;

    @JsonProperty("contract_address")
    private final String contractAddress;

    @JsonProperty("has_balance")
    private final Boolean hasBalance;

    @JsonProperty("min_confirmation")
    private final Integer minConfirmation;

    @JsonProperty("icon")
    private final Icon icon;

    @JsonProperty("token_icon")
    private final Icon tokenIcon;

    @JsonProperty("blockchain_icon")
    private final Icon blockchainIcon;

    @JsonProperty("explorer_link")
    private final String explorerLink;

    /**
     * Constructs a {@code ExtendedCurrency}.
     *
     * @param id              The currency identifier, for example {@code USDT.Tron}.
     * @param code            The currency ticker, for example {@code USDT}.
     * @param name            The display name of the currency.
     * @param blockchain      The blockchain the currency lives on.
     * @param contractAddress The token contract address, empty for native coins.
     * @param hasBalance      Whether the store currently holds a balance in this currency.
     * @param minConfirmation The number of blockchain confirmations required to credit a payment.
     * @param icon            The currency artwork.
     * @param tokenIcon       The token artwork.
     * @param blockchainIcon  The blockchain artwork.
     * @param explorerLink    The block explorer URL template for this currency.
     */
    @JsonCreator
    public ExtendedCurrency(@JsonProperty("id") final String id,
                            @JsonProperty("code") final String code,
                            @JsonProperty("name") final String name,
                            @JsonProperty("blockchain") final String blockchain,
                            @JsonProperty("contract_address") final String contractAddress,
                            @JsonProperty("has_balance") final Boolean hasBalance,
                            @JsonProperty("min_confirmation") final Integer minConfirmation,
                            @JsonProperty("icon") final Icon icon,
                            @JsonProperty("token_icon") final Icon tokenIcon,
                            @JsonProperty("blockchain_icon") final Icon blockchainIcon,
                            @JsonProperty("explorer_link") final String explorerLink) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.blockchain = blockchain;
        this.contractAddress = contractAddress;
        this.hasBalance = hasBalance;
        this.minConfirmation = minConfirmation;
        this.icon = icon;
        this.tokenIcon = tokenIcon;
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
     * Returns whether the store currently holds a balance in this currency.
     *
     * @return Whether the store currently holds a balance in this currency.
     */
    public Boolean getHasBalance() {
        return hasBalance;
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
     * Returns the token artwork.
     *
     * @return The token artwork.
     */
    public Icon getTokenIcon() {
        return tokenIcon;
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
        if (!(other instanceof ExtendedCurrency)) {
            return false;
        }

        ExtendedCurrency that = (ExtendedCurrency) other;

        return Objects.equals(id, that.id)
                && Objects.equals(code, that.code)
                && Objects.equals(name, that.name)
                && Objects.equals(blockchain, that.blockchain)
                && Objects.equals(contractAddress, that.contractAddress)
                && Objects.equals(hasBalance, that.hasBalance)
                && Objects.equals(minConfirmation, that.minConfirmation)
                && Objects.equals(icon, that.icon)
                && Objects.equals(tokenIcon, that.tokenIcon)
                && Objects.equals(blockchainIcon, that.blockchainIcon)
                && Objects.equals(explorerLink, that.explorerLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, blockchain, contractAddress, hasBalance, minConfirmation, icon,
                            tokenIcon, blockchainIcon, explorerLink);
    }

    @Override
    public String toString() {
        return "ExtendedCurrency{"
                + "id=" + id
                + ", code=" + code
                + ", name=" + name
                + ", blockchain=" + blockchain
                + ", contractAddress=" + contractAddress
                + ", hasBalance=" + hasBalance
                + ", minConfirmation=" + minConfirmation
                + ", icon=" + icon
                + ", tokenIcon=" + tokenIcon
                + ", blockchainIcon=" + blockchainIcon
                + ", explorerLink=" + explorerLink
                + '}';
    }
}
