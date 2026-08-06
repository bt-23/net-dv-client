package net.dv.client.services.withdrawals.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The parameters of a withdrawal from a processing wallet.
 *
 * <p>{@code requestId} is your own identifier for the payout and it makes the call idempotent:
 * retrying with the same value never sends the funds twice, and the same value can be passed to
 * {@link net.dv.client.services.withdrawals.Withdrawals#get} to look the withdrawal up.</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CreateWithdrawalOptions {

    @JsonProperty("address_to")
    private final String addressTo;

    @JsonProperty("currency_id")
    private final String currencyId;

    @JsonProperty("amount")
    private final String amount;

    @JsonProperty("request_id")
    private final String requestId;

    private CreateWithdrawalOptions(final Builder builder) {
        this.addressTo = builder.addressTo;
        this.currencyId = builder.currencyId;
        this.amount = builder.amount;
        this.requestId = builder.requestId;
    }

    /**
     * Returns the address the funds are sent to.
     *
     * @return The destination address.
     */
    public String getAddressTo() {
        return addressTo;
    }

    /**
     * Returns the currency being withdrawn.
     *
     * @return The currency identifier, for example {@code USDT.Tron}.
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * Returns the amount being withdrawn.
     *
     * @return The amount, as a decimal string.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Returns your own identifier for the payout.
     *
     * @return The request identifier.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Creates a builder for the required parameters, all of which the API mandates.
     *
     * @param addressTo  The address the funds are sent to.
     * @param currencyId The currency to withdraw, for example {@code USDT.Tron}.
     * @param amount     The amount to withdraw, as a decimal string.
     * @param requestId  Your own identifier for the payout, which makes the call idempotent.
     * @return A new builder instance.
     */
    public static Builder builder(final String addressTo, final String currencyId, final String amount,
                                  final String requestId) {
        return new Builder(addressTo, currencyId, amount, requestId);
    }

    /**
     * Builder for {@link CreateWithdrawalOptions}.
     */
    public static final class Builder {

        private final String addressTo;
        private final String currencyId;
        private final String amount;
        private final String requestId;

        private Builder(final String addressTo, final String currencyId, final String amount,
                        final String requestId) {
            this.addressTo = Objects.requireNonNull(addressTo, "addressTo");
            this.currencyId = Objects.requireNonNull(currencyId, "currencyId");
            this.amount = Objects.requireNonNull(amount, "amount");
            this.requestId = Objects.requireNonNull(requestId, "requestId");
        }

        /**
         * Builds the options.
         *
         * @return A new {@link CreateWithdrawalOptions} instance.
         */
        public CreateWithdrawalOptions build() {
            return new CreateWithdrawalOptions(this);
        }
    }
}
