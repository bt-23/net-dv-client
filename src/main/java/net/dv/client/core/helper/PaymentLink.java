package net.dv.client.core.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Builds the URL of the hosted payment page.
 *
 * <p>{@link net.dv.client.services.wallets.Wallets#create} already returns a ready to use
 * {@code payUrl}. This helper is for the cases where you want to send a customer to the page
 * without creating the wallet first, for example from a link in an email.</p>
 */
public final class PaymentLink {

    private PaymentLink() {
    }

    /**
     * Builds the payment page URL for a customer.
     *
     * @param host            The payment page host, for example {@code https://pay.example.com}.
     * @param storeId         The store identifier.
     * @param storeExternalId The identifier of the customer in your own system.
     * @return The payment page URL.
     */
    public static String of(final String host, final String storeId, final String storeExternalId) {
        return of(host, storeId, storeExternalId, null);
    }

    /**
     * Builds the payment page URL for a customer, prefilling the email field.
     *
     * @param host            The payment page host, for example {@code https://pay.example.com}.
     * @param storeId         The store identifier.
     * @param storeExternalId The identifier of the customer in your own system.
     * @param email           The customer email, may be {@code null}.
     * @return The payment page URL.
     */
    public static String of(final String host, final String storeId, final String storeExternalId,
                            final String email) {
        Objects.requireNonNull(host, "host");
        Objects.requireNonNull(storeId, "storeId");
        Objects.requireNonNull(storeExternalId, "storeExternalId");

        String base = host.endsWith("/") ? host.substring(0, host.length() - 1) : host;
        String url = base + "/" + encode(storeId) + "/" + encode(storeExternalId);

        return Strings.isBlank(email) ? url : url + "?email=" + encode(email);
    }

    private static String encode(final String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 is required by every Java platform.", e);
        }
    }
}
