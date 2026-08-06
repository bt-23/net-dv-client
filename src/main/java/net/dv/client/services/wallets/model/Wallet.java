package net.dv.client.services.wallets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The wallet issued to one of your customers, holding a deposit address per enabled currency.
 *
 * <p>The same {@code storeExternalId} always resolves to the same wallet, so calling the endpoint
 * again for a returning customer returns the addresses that were already issued.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Wallet {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("store_id")
    private final String storeId;

    @JsonProperty("store_external_id")
    private final String storeExternalId;

    @JsonProperty("pay_url")
    private final String payUrl;

    @JsonProperty("amount_usd")
    private final String amountUsd;

    @JsonProperty("address")
    private final List<WalletAddress> addresses;

    @JsonProperty("rates")
    private final Map<String, String> rates;

    @JsonProperty("created_at")
    private final OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private final OffsetDateTime updatedAt;

    /**
     * Constructs a {@code Wallet}.
     *
     * @param id              The wallet identifier.
     * @param storeId         The identifier of the store the wallet belongs to.
     * @param storeExternalId The identifier of the customer in your own system.
     * @param payUrl          The hosted payment page to redirect the customer to.
     * @param amountUsd       The invoiced amount in USD, empty when no amount was requested.
     * @param addresses       The deposit addresses issued to the customer.
     * @param rates           The exchange rates quoted for the wallet, keyed by currency identifier.
     * @param createdAt       The moment the wallet was created.
     * @param updatedAt       The moment the wallet was last changed.
     */
    @JsonCreator
    public Wallet(@JsonProperty("id") final String id,
                  @JsonProperty("store_id") final String storeId,
                  @JsonProperty("store_external_id") final String storeExternalId,
                  @JsonProperty("pay_url") final String payUrl,
                  @JsonProperty("amount_usd") final String amountUsd,
                  @JsonProperty("address") final List<WalletAddress> addresses,
                  @JsonProperty("rates") final Map<String, String> rates,
                  @JsonProperty("created_at") final OffsetDateTime createdAt,
                  @JsonProperty("updated_at") final OffsetDateTime updatedAt) {
        this.id = id;
        this.storeId = storeId;
        this.storeExternalId = storeExternalId;
        this.payUrl = payUrl;
        this.amountUsd = amountUsd;
        this.addresses = addresses;
        this.rates = rates;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the wallet identifier.
     *
     * @return The wallet identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the identifier of the store the wallet belongs to.
     *
     * @return The identifier of the store the wallet belongs to.
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * Returns the identifier of the customer in your own system.
     *
     * @return The identifier of the customer in your own system.
     */
    public String getStoreExternalId() {
        return storeExternalId;
    }

    /**
     * Returns the hosted payment page to redirect the customer to.
     *
     * @return The hosted payment page to redirect the customer to.
     */
    public String getPayUrl() {
        return payUrl;
    }

    /**
     * Returns the invoiced amount in USD, empty when no amount was requested.
     *
     * @return The invoiced amount in USD, empty when no amount was requested.
     */
    public String getAmountUsd() {
        return amountUsd;
    }

    /**
     * Returns the deposit addresses issued to the customer.
     *
     * @return The deposit addresses issued to the customer.
     */
    public List<WalletAddress> getAddresses() {
        return addresses;
    }

    /**
     * Returns the exchange rates quoted for the wallet, keyed by currency identifier.
     *
     * @return The exchange rates quoted for the wallet, keyed by currency identifier.
     */
    public Map<String, String> getRates() {
        return rates;
    }

    /**
     * Returns the moment the wallet was created.
     *
     * @return The moment the wallet was created.
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the moment the wallet was last changed.
     *
     * @return The moment the wallet was last changed.
     */
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Wallet)) {
            return false;
        }

        Wallet that = (Wallet) other;

        return Objects.equals(id, that.id)
                && Objects.equals(storeId, that.storeId)
                && Objects.equals(storeExternalId, that.storeExternalId)
                && Objects.equals(payUrl, that.payUrl)
                && Objects.equals(amountUsd, that.amountUsd)
                && Objects.equals(addresses, that.addresses)
                && Objects.equals(rates, that.rates)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeId, storeExternalId, payUrl, amountUsd, addresses, rates, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Wallet{"
                + "id=" + id
                + ", storeId=" + storeId
                + ", storeExternalId=" + storeExternalId
                + ", payUrl=" + payUrl
                + ", amountUsd=" + amountUsd
                + ", addresses=" + addresses
                + ", rates=" + rates
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
