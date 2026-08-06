package net.dv.client.services.webhooks.model;

import java.util.Objects;

/**
 * The wallet a webhook refers to.
 */
public final class WebhookWallet {

    private final String id;
    private final String storeExternalId;

    /**
     * Constructs a {@code WebhookWallet}.
     *
     * @param id              The wallet identifier in the merchant system.
     * @param storeExternalId The identifier of the customer in your own system, as supplied when the wallet was
     *                       created. This is what links the event back to your order.
     */
    public WebhookWallet(final String id,
                         final String storeExternalId) {
        this.id = id;
        this.storeExternalId = storeExternalId;
    }

    /**
     * Returns the wallet identifier in the merchant system.
     *
     * @return The wallet identifier in the merchant system.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the identifier of the customer in your own system, as supplied when the wallet was created. This is
     * what links the event back to your order.
     *
     * @return The identifier of the customer in your own system, as supplied when the wallet was created. This is
     *         what links the event back to your order.
     */
    public String getStoreExternalId() {
        return storeExternalId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WebhookWallet)) {
            return false;
        }

        WebhookWallet that = (WebhookWallet) other;

        return Objects.equals(id, that.id)
                && Objects.equals(storeExternalId, that.storeExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeExternalId);
    }

    @Override
    public String toString() {
        return "WebhookWallet{"
                + "id=" + id
                + ", storeExternalId=" + storeExternalId
                + '}';
    }
}
