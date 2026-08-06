package net.dv.client.services.currencies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dv.client.services.common.model.Icon;

import java.util.List;
import java.util.Objects;

/**
 * A token such as USDT grouped with every blockchain it is issued on, which lets a payment form
 * offer the token first and the network second.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TokenGroup {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("icon")
    private final Icon icon;

    @JsonProperty("currencies")
    private final List<String> currencies;

    @JsonProperty("blockchains")
    private final List<String> blockchains;

    /**
     * Constructs a {@code TokenGroup}.
     *
     * @param name        The token name, for example {@code USDT}.
     * @param icon        The token artwork.
     * @param currencies  The identifiers of the currencies belonging to this token.
     * @param blockchains The blockchains this token is available on.
     */
    @JsonCreator
    public TokenGroup(@JsonProperty("name") final String name,
                      @JsonProperty("icon") final Icon icon,
                      @JsonProperty("currencies") final List<String> currencies,
                      @JsonProperty("blockchains") final List<String> blockchains) {
        this.name = name;
        this.icon = icon;
        this.currencies = currencies;
        this.blockchains = blockchains;
    }

    /**
     * Returns the token name, for example {@code USDT}.
     *
     * @return The token name, for example {@code USDT}.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the token artwork.
     *
     * @return The token artwork.
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Returns the identifiers of the currencies belonging to this token.
     *
     * @return The identifiers of the currencies belonging to this token.
     */
    public List<String> getCurrencies() {
        return currencies;
    }

    /**
     * Returns the blockchains this token is available on.
     *
     * @return The blockchains this token is available on.
     */
    public List<String> getBlockchains() {
        return blockchains;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TokenGroup)) {
            return false;
        }

        TokenGroup that = (TokenGroup) other;

        return Objects.equals(name, that.name)
                && Objects.equals(icon, that.icon)
                && Objects.equals(currencies, that.currencies)
                && Objects.equals(blockchains, that.blockchains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, icon, currencies, blockchains);
    }

    @Override
    public String toString() {
        return "TokenGroup{"
                + "name=" + name
                + ", icon=" + icon
                + ", currencies=" + currencies
                + ", blockchains=" + blockchains
                + '}';
    }
}
