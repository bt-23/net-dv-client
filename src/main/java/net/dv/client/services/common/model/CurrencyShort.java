package net.dv.client.services.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The condensed currency descriptor embedded in balance payloads.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CurrencyShort {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("code")
    private final String code;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("blockchain")
    private final String blockchain;

    @JsonProperty("sort_order")
    private final Integer sortOrder;

    /**
     * Constructs a {@code CurrencyShort}.
     *
     * @param id         The currency identifier, for example {@code USDT.Tron}.
     * @param code       The currency ticker, for example {@code USDT}.
     * @param name       The display name of the currency.
     * @param blockchain The blockchain the currency lives on.
     * @param sortOrder  The display order suggested by the merchant, may be {@code null}.
     */
    @JsonCreator
    public CurrencyShort(@JsonProperty("id") final String id,
                         @JsonProperty("code") final String code,
                         @JsonProperty("name") final String name,
                         @JsonProperty("blockchain") final String blockchain,
                         @JsonProperty("sort_order") final Integer sortOrder) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.blockchain = blockchain;
        this.sortOrder = sortOrder;
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
     * Returns the display order suggested by the merchant, may be {@code null}.
     *
     * @return The display order suggested by the merchant, may be {@code null}.
     */
    public Integer getSortOrder() {
        return sortOrder;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CurrencyShort)) {
            return false;
        }

        CurrencyShort that = (CurrencyShort) other;

        return Objects.equals(id, that.id)
                && Objects.equals(code, that.code)
                && Objects.equals(name, that.name)
                && Objects.equals(blockchain, that.blockchain)
                && Objects.equals(sortOrder, that.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, blockchain, sortOrder);
    }

    @Override
    public String toString() {
        return "CurrencyShort{"
                + "id=" + id
                + ", code=" + code
                + ", name=" + name
                + ", blockchain=" + blockchain
                + ", sortOrder=" + sortOrder
                + '}';
    }
}
