package net.dv.client.services.wallets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * A deposit address issued to a customer for one currency.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WalletAddress {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("wallet_id")
    private final String walletId;

    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("currency_id")
    private final String currencyId;

    @JsonProperty("blockchain")
    private final String blockchain;

    @JsonProperty("address")
    private final String address;

    @JsonProperty("dirty")
    private final Boolean dirty;

    @JsonProperty("created_at")
    private final OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private final OffsetDateTime updatedAt;

    @JsonProperty("deleted_at")
    private final OffsetDateTime deletedAt;

    /**
     * Constructs a {@code WalletAddress}.
     *
     * @param id         The address identifier.
     * @param walletId   The identifier of the wallet the address belongs to.
     * @param userId     The identifier of the merchant user owning the wallet.
     * @param currencyId The currency the address accepts, for example {@code USDT.Tron}.
     * @param blockchain The blockchain the address lives on.
     * @param address    The blockchain address the customer must pay to.
     * @param dirty      Whether the address was flagged and is no longer offered for new payments.
     * @param createdAt  The moment the address was issued.
     * @param updatedAt  The moment the address was last changed.
     * @param deletedAt  The moment the address was retired, {@code null} while it is active.
     */
    @JsonCreator
    public WalletAddress(@JsonProperty("id") final String id,
                         @JsonProperty("wallet_id") final String walletId,
                         @JsonProperty("user_id") final String userId,
                         @JsonProperty("currency_id") final String currencyId,
                         @JsonProperty("blockchain") final String blockchain,
                         @JsonProperty("address") final String address,
                         @JsonProperty("dirty") final Boolean dirty,
                         @JsonProperty("created_at") final OffsetDateTime createdAt,
                         @JsonProperty("updated_at") final OffsetDateTime updatedAt,
                         @JsonProperty("deleted_at") final OffsetDateTime deletedAt) {
        this.id = id;
        this.walletId = walletId;
        this.userId = userId;
        this.currencyId = currencyId;
        this.blockchain = blockchain;
        this.address = address;
        this.dirty = dirty;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * Returns the address identifier.
     *
     * @return The address identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the identifier of the wallet the address belongs to.
     *
     * @return The identifier of the wallet the address belongs to.
     */
    public String getWalletId() {
        return walletId;
    }

    /**
     * Returns the identifier of the merchant user owning the wallet.
     *
     * @return The identifier of the merchant user owning the wallet.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the currency the address accepts, for example {@code USDT.Tron}.
     *
     * @return The currency the address accepts, for example {@code USDT.Tron}.
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Returns the blockchain the address lives on.
     *
     * @return The blockchain the address lives on.
     */
    public String getBlockchain() {
        return blockchain;
    }

    /**
     * Returns the blockchain address the customer must pay to.
     *
     * @return The blockchain address the customer must pay to.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns whether the address was flagged and is no longer offered for new payments.
     *
     * @return Whether the address was flagged and is no longer offered for new payments.
     */
    public Boolean getDirty() {
        return dirty;
    }

    /**
     * Returns the moment the address was issued.
     *
     * @return The moment the address was issued.
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the moment the address was last changed.
     *
     * @return The moment the address was last changed.
     */
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns the moment the address was retired, {@code null} while it is active.
     *
     * @return The moment the address was retired, {@code null} while it is active.
     */
    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WalletAddress)) {
            return false;
        }

        WalletAddress that = (WalletAddress) other;

        return Objects.equals(id, that.id)
                && Objects.equals(walletId, that.walletId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(currencyId, that.currencyId)
                && Objects.equals(blockchain, that.blockchain)
                && Objects.equals(address, that.address)
                && Objects.equals(dirty, that.dirty)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt)
                && Objects.equals(deletedAt, that.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, walletId, userId, currencyId, blockchain, address, dirty, createdAt, updatedAt,
                            deletedAt);
    }

    @Override
    public String toString() {
        return "WalletAddress{"
                + "id=" + id
                + ", walletId=" + walletId
                + ", userId=" + userId
                + ", currencyId=" + currencyId
                + ", blockchain=" + blockchain
                + ", address=" + address
                + ", dirty=" + dirty
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", deletedAt=" + deletedAt
                + '}';
    }
}
