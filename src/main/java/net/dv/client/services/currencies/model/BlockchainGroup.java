package net.dv.client.services.currencies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv.client.services.common.model.Icon;

import java.util.List;
import java.util.Objects;

/**
 * A blockchain grouped with every currency and token available on it, which lets a payment form
 * offer the network first and the token second.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class BlockchainGroup {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("icon")
    private final Icon icon;

    @JsonProperty("currencies")
    private final List<String> currencies;

    @JsonProperty("tokens")
    private final List<String> tokens;

    /**
     * Constructs a {@code BlockchainGroup}.
     *
     * @param name       The blockchain name, for example {@code Tron}.
     * @param icon       The blockchain artwork.
     * @param currencies The identifiers of the currencies available on this blockchain.
     * @param tokens     The tokens available on this blockchain.
     */
    @JsonCreator
    public BlockchainGroup(@JsonProperty("name") final String name,
                           @JsonProperty("icon") final Icon icon,
                           @JsonProperty("currencies") final List<String> currencies,
                           @JsonProperty("tokens") final List<String> tokens) {
        this.name = name;
        this.icon = icon;
        this.currencies = currencies;
        this.tokens = tokens;
    }

    /**
     * Returns the blockchain name, for example {@code Tron}.
     *
     * @return The blockchain name, for example {@code Tron}.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the blockchain artwork.
     *
     * @return The blockchain artwork.
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Returns the identifiers of the currencies available on this blockchain.
     *
     * @return The identifiers of the currencies available on this blockchain.
     */
    public List<String> getCurrencies() {
        return currencies;
    }

    /**
     * Returns the tokens available on this blockchain.
     *
     * @return The tokens available on this blockchain.
     */
    public List<String> getTokens() {
        return tokens;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BlockchainGroup)) {
            return false;
        }

        BlockchainGroup that = (BlockchainGroup) other;

        return Objects.equals(name, that.name)
                && Objects.equals(icon, that.icon)
                && Objects.equals(currencies, that.currencies)
                && Objects.equals(tokens, that.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, icon, currencies, tokens);
    }

    @Override
    public String toString() {
        return "BlockchainGroup{"
                + "name=" + name
                + ", icon=" + icon
                + ", currencies=" + currencies
                + ", tokens=" + tokens
                + '}';
    }
}
