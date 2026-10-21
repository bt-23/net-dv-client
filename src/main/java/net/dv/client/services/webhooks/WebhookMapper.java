package net.dv.client.services.webhooks;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv.client.core.exception.DvNetInvalidWebhookException;
import net.dv.client.core.helper.DateTimes;
import net.dv.client.services.webhooks.model.AmlCheck;
import net.dv.client.services.webhooks.model.PaymentAmlBlockedEvent;
import net.dv.client.services.webhooks.model.PaymentNotConfirmedEvent;
import net.dv.client.services.webhooks.model.PaymentReceivedEvent;
import net.dv.client.services.webhooks.model.WebhookEvent;
import net.dv.client.services.webhooks.model.WebhookTransaction;
import net.dv.client.services.webhooks.model.WebhookType;
import net.dv.client.services.webhooks.model.WebhookWallet;
import net.dv.client.services.webhooks.model.WithdrawalEvent;

import java.time.OffsetDateTime;

/**
 * Turns a raw webhook document into a {@link WebhookEvent}.
 *
 * <p>The shapes follow the DV.net server rather than the published documentation:</p>
 * <ul>
 *     <li>a mempool notification prefixes every member, nested ones included, with
 *     {@code unconfirmed_};</li>
 *     <li>a withdrawal notification carries {@code withdrawal_id} but neither {@code status} nor
 *     {@code wallet};</li>
 *     <li>an AML blocked notification carries an {@code aml_check} member;</li>
 *     <li>anything else carrying a {@code type} is a confirmed payment.</li>
 * </ul>
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
                WebhookType type = WebhookType.fromValue(rawType);

                if (type == WebhookType.WITHDRAWAL_FROM_PROCESSING_RECEIVED || root.has("withdrawal_id")) {
                    return mapWithdrawal(root, rawType);
                }
                if (type == WebhookType.PAYMENT_AML_BLOCKED || root.has("aml_check")) {
                    return mapPaymentAmlBlocked(root, rawType);
                }

                return mapPaymentReceived(root, rawType);
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

    private PaymentAmlBlockedEvent mapPaymentAmlBlocked(final JsonNode root, final String rawType) {
        return new PaymentAmlBlockedEvent(
                WebhookType.fromValue(rawType),
                rawType,
                text(root, "status"),
                text(root, "amount"),
                timestamp(root, "created_at"),
                timestamp(root, "paid_at"),
                mapTransaction(root.get("transactions"), ""),
                mapWallet(root.get("wallet"), ""),
                mapAmlCheck(root.get("aml_check")));
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

    private AmlCheck mapAmlCheck(final JsonNode node) {
        if (node == null || !node.isObject()) {
            return null;
        }

        return new AmlCheck(
                text(node, "id"),
                text(node, "score"),
                text(node, "status"),
                text(node, "risk_level"),
                timestamp(node, "created_at"),
                timestamp(node, "updated_at"));
    }

    private static String text(final JsonNode node, final String member) {
        JsonNode value = node.get(member);
        return value == null || value.isNull() ? null : value.asText();
    }

    private static OffsetDateTime timestamp(final JsonNode node, final String member) {
        return DateTimes.parse(text(node, member));
    }
}
