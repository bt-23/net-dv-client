package net.dv.client.services.currencies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * The extended store catalogue, exposing the same currencies grouped by token and by blockchain.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ExtendedCurrencies {

    @JsonProperty("tokens")
    private final List<TokenGroup> tokens;

    @JsonProperty("blockchains")
    private final List<BlockchainGroup> blockchains;

    @JsonProperty("currencies")
    private final List<ExtendedCurrency> currencies;

    /**
     * Constructs a {@code ExtendedCurrencies}.
     *
     * @param tokens      The currencies grouped by token.
     * @param blockchains The currencies grouped by blockchain.
     * @param currencies  The flat list of currencies.
     */
    @JsonCreator
    public ExtendedCurrencies(@JsonProperty("tokens") final List<TokenGroup> tokens,
                              @JsonProperty("blockchains") final List<BlockchainGroup> blockchains,
                              @JsonProperty("currencies") final List<ExtendedCurrency> currencies) {
        this.tokens = tokens;
        this.blockchains = blockchains;
        this.currencies = currencies;
    }

    /**
     * Returns the currencies grouped by token.
     *
     * @return The currencies grouped by token.
     */
    public List<TokenGroup> getTokens() {
        return tokens;
    }

    /**
     * Returns the currencies grouped by blockchain.
     *
     * @return The currencies grouped by blockchain.
     */
    public List<BlockchainGroup> getBlockchains() {
        return blockchains;
    }

    /**
     * Returns the flat list of currencies.
     *
     * @return The flat list of currencies.
     */
    public List<ExtendedCurrency> getCurrencies() {
        return currencies;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExtendedCurrencies)) {
            return false;
        }

        ExtendedCurrencies that = (ExtendedCurrencies) other;

        return Objects.equals(tokens, that.tokens)
                && Objects.equals(blockchains, that.blockchains)
                && Objects.equals(currencies, that.currencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens, blockchains, currencies);
    }

    @Override
    public String toString() {
        return "ExtendedCurrencies{"
                + "tokens=" + tokens
                + ", blockchains=" + blockchains
                + ", currencies=" + currencies
                + '}';
    }
}
