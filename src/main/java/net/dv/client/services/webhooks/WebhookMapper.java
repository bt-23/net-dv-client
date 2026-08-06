package net.dv.client.services.webhooks;

import net.dv.client.core.exception.DvNetInvalidWebhookException;
import net.dv.client.core.helper.DateTimes;
import net.dv.client.services.webhooks.model.PaymentNotConfirmedEvent;
import net.dv.client.services.webhooks.model.PaymentReceivedEvent;
import net.dv.client.services.webhooks.model.WebhookEvent;
import net.dv.client.services.webhooks.model.WebhookTransaction;
import net.dv.client.services.webhooks.model.WebhookType;
import net.dv.client.services.webhooks.model.WebhookWallet;
import com.fasterxml.jackson.databind.JsonNode;
import net.dv.client.services.webhooks.model.WithdrawalEvent;

/**
 * Turns a raw webhook document into a {@link WebhookEvent}.
 *
 * <p>The three webhook shapes are told apart by their members rather than by a discriminator: a
 * mempool notification prefixes every member with {@code unconfirmed_}, a withdrawal notification
 * announces the withdrawal type, and anything else carrying a {@code type} is a confirmed
 * payment.</p>
 */
public class WebhookMapper {

    private static final String UNCONFIRMED_PREFIX = "unconfirmed_";

    /**
     * Maps a parsed webhook document.
     *
     * @param root The parsed webhook document.
     * @return The decoded event.
     * @throws DvNetInvalidWebhookException If the document matches none of the known shapes, or if
     *                                      a member cannot be read.
     */
    public WebhookEvent map(final JsonNode root) throws DvNetInvalidWebhookException {
        if (root == null || !root.isObject()) {
            throw new DvNetInvalidWebhookException("The webhook payload is not a JSON object.");
        }

        try {
            if (root.has(UNCONFIRMED_PREFIX + "type")) {
                return mapPaymentNotConfirmed(root);
            }

            if (root.has("type")) {
                String rawType = text(root, "type");
                boolean isWithdrawal = root.has("withdrawal_id")
                        || WebhookType.WITHDRAWAL_FROM_PROCESSING_RECEIVED.getValue().equals(rawType);

                return isWithdrawal ? mapWithdrawal(root, rawType) : mapPaymentReceived(root, rawType);
            }
        } catch (RuntimeException e) {
            throw new DvNetInvalidWebhookException("Unable to map the webhook payload: " + e.getMessage(), e);
        }

        throw new DvNetInvalidWebhookException(
                "Unknown webhook format, the payload carries neither a \"type\" nor an \"unconfirmed_type\" member.");
    }

    private PaymentReceivedEvent mapPaymentReceived(final JsonNode root, final String rawType) {
        return new PaymentReceivedEvent(
                WebhookType.fromValue(rawType),
                rawType,
                text(root, "status"),
                text(root, "amount"),
                timestamp(root, "created_at"),
                timestamp(root, "paid_at"),
                mapTransaction(root.get("transactions"), ""),
                mapWallet(root.get("wallet"), ""));
    }

    private WithdrawalEvent mapWithdrawal(final JsonNode root, final String rawType) {
        return new WithdrawalEvent(
                WebhookType.fromValue(rawType),
                rawType,
                text(root, "status"),
                text(root, "amount"),
                timestamp(root, "created_at"),
                timestamp(root, "paid_at"),
                mapTransaction(root.get("transactions"), ""),
                mapWallet(root.get("wallet"), ""),
                text(root, "withdrawal_id"));
    }

    private PaymentNotConfirmedEvent mapPaymentNotConfirmed(final JsonNode root) {
        String rawType = text(root, UNCONFIRMED_PREFIX + "type");

        return new PaymentNotConfirmedEvent(
                WebhookType.fromValue(rawType),
                rawType,
                text(root, UNCONFIRMED_PREFIX + "status"),
                text(root, UNCONFIRMED_PREFIX + "amount"),
                timestamp(root, UNCONFIRMED_PREFIX + "created_at"),
                timestamp(root, UNCONFIRMED_PREFIX + "paid_at"),
                mapTransaction(root.get(UNCONFIRMED_PREFIX + "transactions"), UNCONFIRMED_PREFIX),
                mapWallet(root.get(UNCONFIRMED_PREFIX + "wallet"), UNCONFIRMED_PREFIX));
    }

    private WebhookTransaction mapTransaction(final JsonNode node, final String prefix) {
        if (node == null || !node.isObject()) {
            return null;
        }

        return new WebhookTransaction(
                text(node, prefix + "tx_id"),
                text(node, prefix + "tx_hash"),
                text(node, prefix + "bc_uniq_key"),
                text(node, prefix + "currency"),
                text(node, prefix + "currency_id"),
                text(node, prefix + "blockchain"),
                text(node, prefix + "amount"),
                text(node, prefix + "amount_usd"),
                timestamp(node, prefix + "created_at"));
    }

    private WebhookWallet mapWallet(final JsonNode node, final String prefix) {
        if (node == null || !node.isObject()) {
            return null;
        }

        return new WebhookWallet(
                text(node, prefix + "id"),
                text(node, prefix + "store_external_id"));
    }

    private static String text(final JsonNode node, final String member) {
        JsonNode value = node.get(member);
        return value == null || value.isNull() ? null : value.asText();
    }

    private static java.time.OffsetDateTime timestamp(final JsonNode node, final String member) {
        return DateTimes.parse(text(node, member));
    }
}
