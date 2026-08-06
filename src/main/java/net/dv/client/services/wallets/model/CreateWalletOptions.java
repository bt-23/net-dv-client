package net.dv.client.services.wallets.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The parameters used to issue deposit addresses to a customer.
 *
 * <p>Only {@code storeExternalId} is required: it is the identifier of the customer in your own
 * system and it is echoed back on every webhook, which is what lets you match an incoming payment
 * to an order.</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CreateWalletOptions {

    @JsonProperty("store_external_id")
    private final String storeExternalId;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("ip")
    private final String ip;

    @JsonProperty("amount")
    private final String amount;

    @JsonProperty("currency")
    private final String currency;

    @JsonProperty("locale")
    private final String locale;

    private CreateWalletOptions(final Builder builder) {
        this.storeExternalId = builder.storeExternalId;
        this.email = builder.email;
        this.ip = builder.ip;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.locale = builder.locale;
    }

    /**
     * Returns the identifier of the customer in your own system.
     *
     * @return The external customer identifier.
     */
    public String getStoreExternalId() {
        return storeExternalId;
    }

    /**
     * Returns the email of the customer.
     *
     * @return The customer email, or {@code null} when it was not supplied.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the IP address of the customer.
     *
     * @return The customer IP address, or {@code null} when it was not supplied.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Returns the amount to invoice.
     *
     * @return The amount, or {@code null} when the customer may pay any amount.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Returns the currency the amount is expressed in.
     *
     * @return The currency, or {@code null} when it was not supplied.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the locale of the hosted payment page.
     *
     * @return The locale, or {@code null} when the store default applies.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Creates a builder for the required customer identifier.
     *
     * @param storeExternalId The identifier of the customer in your own system.
     * @return A new builder instance.
     */
    public static Builder builder(final String storeExternalId) {
        return new Builder(storeExternalId);
    }

    /**
     * Builder for {@link CreateWalletOptions}.
     */
    public static final class Builder {

        private final String storeExternalId;
        private String email;
        private String ip;
        private String amount;
        private String currency;
        private String locale;

        private Builder(final String storeExternalId) {
            this.storeExternalId = Objects.requireNonNull(storeExternalId, "storeExternalId");
        }

        /**
         * Sets the email of the customer, used by the merchant to send payment notifications.
         *
         * @param email The customer email.
         * @return The builder instance.
         */
        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the IP address of the customer, used for risk scoring.
         *
         * @param ip The customer IP address.
         * @return The builder instance.
         */
        public Builder ip(final String ip) {
            this.ip = ip;
            return this;
        }

        /**
         * Sets the amount to invoice, as a decimal string to preserve precision.
         *
         * @param amount The amount to invoice.
         * @return The builder instance.
         */
        public Builder amount(final String amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Sets the currency the invoiced amount is expressed in.
         *
         * @param currency The currency code.
         * @return The builder instance.
         */
        public Builder currency(final String currency) {
            this.currency = currency;
            return this;
        }

        /**
         * Sets the locale of the hosted payment page.
         *
         * @param locale The locale, for example {@code en}.
         * @return The builder instance.
         */
        public Builder locale(final String locale) {
            this.locale = locale;
            return this;
        }

        /**
         * Builds the options.
         *
         * @return A new {@link CreateWalletOptions} instance.
         */
        public CreateWalletOptions build() {
            return new CreateWalletOptions(this);
        }
    }
}
