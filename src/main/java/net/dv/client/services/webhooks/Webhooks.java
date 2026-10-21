package net.dv.client.services.webhooks;

import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.exception.DvNetInvalidResponseDataException;
import net.dv.client.core.exception.DvNetInvalidWebhookException;
import net.dv.client.core.mapper.DvNetJsonMapper;
import net.dv.client.core.mapper.JsonMapper;
import net.dv.client.services.webhooks.model.WebhookEvent;

import net.dv.client.core.helper.Strings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Verifies and decodes the webhooks DV.net delivers.
 *
 * <p>Unlike the other services this one performs no HTTP call: it works on the request body your
 * own endpoint received.</p>
 *
 * <p>Always verify before decoding, and always verify against the body exactly as it arrived. Any
 * re-serialization changes the bytes and the signature will no longer match.</p>
 *
 * <p>DV.net treats a delivery as acknowledged only when the response body is the JSON document
 * {@value #SUCCESS_RESPONSE}; the HTTP status code alone is not enough. Anything else, including an
 * empty {@code 200}, is retried with an exponential back-off starting at one minute and capped at
 * one day, up to thirty attempts by default. Reply with {@link #SUCCESS_RESPONSE} once the event is
 * stored, and also for duplicates you already processed.</p>
 *
 * <pre>{@code
 * String body = readRequestBody(request);
 * String signature = request.getHeader(Webhooks.SIGNATURE_HEADER);
 *
 * WebhookEvent event = dvNet.webhooks().constructEvent(body, signature, webhookSecret);
 *
 * if (event instanceof PaymentReceivedEvent) {
 *     fulfilOrder(event.getWallet().getStoreExternalId());
 * } else if (event instanceof PaymentNotConfirmedEvent) {
 *     markPending(event.getWallet().getStoreExternalId());
 * } else if (event instanceof PaymentAmlBlockedEvent) {
 *     flagForReview(event.getWallet().getStoreExternalId());
 * } else if (event instanceof WithdrawalEvent) {
 *     recordPayout(((WithdrawalEvent) event).getWithdrawalId());
 * }
 *
 * response.setContentType("application/json");
 * response.getWriter().write(Webhooks.SUCCESS_RESPONSE);
 * }</pre>
 */
public final class Webhooks {

    /**
     * The header carrying the webhook signature.
     */
    public static final String SIGNATURE_HEADER = "X-Sign";

    /**
     * The response body DV.net requires to consider a delivery acknowledged.
     */
    public static final String SUCCESS_RESPONSE = "{\"success\":true}";

    private static final String HASH_ALGORITHM = "SHA-256";

    private final JsonMapper jsonMapper;
    private final WebhookMapper webhookMapper;

    /**
     * Constructs the service with the default mappers.
     */
    public Webhooks() {
        this(new DvNetJsonMapper(), new WebhookMapper());
    }

    /**
     * Constructs the service with explicit mappers.
     *
     * @param jsonMapper    The mapper parsing the payload.
     * @param webhookMapper The mapper building the event.
     */
    public Webhooks(final JsonMapper jsonMapper, final WebhookMapper webhookMapper) {
        this.jsonMapper = Objects.requireNonNull(jsonMapper, "jsonMapper");
        this.webhookMapper = Objects.requireNonNull(webhookMapper, "webhookMapper");
    }

    /**
     * Checks that a payload was signed with the store webhook secret.
     *
     * <p>The expected signature is the SHA-256 hash of the raw payload concatenated with the
     * secret, hex encoded. The comparison is constant time.</p>
     *
     * <p>A blank secret is always rejected: without one the signature is a plain hash anybody can
     * compute. A store without a secret also sends an empty {@value #SIGNATURE_HEADER} header for
     * manual resends and dashboard test deliveries, so configure the secret in the merchant
     * settings before going live.</p>
     *
     * @param payload   The raw request body, exactly as received.
     * @param signature The value of the {@value #SIGNATURE_HEADER} header.
     * @param secret    The webhook secret found in the merchant settings.
     * @return {@code true} when the signature matches.
     */
    public boolean verify(final String payload, final String signature, final String secret) {
        if (payload == null || Strings.isBlank(signature) || Strings.isBlank(secret)) {
            return false;
        }

        byte[] expected = sign(payload, secret).getBytes(StandardCharsets.UTF_8);
        byte[] provided = signature.trim().getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(expected, provided);
    }

    /**
     * Computes the signature DV.net is expected to send for a payload.
     *
     * @param payload The raw request body.
     * @param secret  The webhook secret found in the merchant settings.
     * @return The hex encoded SHA-256 signature.
     */
    public String sign(final String payload, final String secret) {
        Objects.requireNonNull(payload, "payload");
        Objects.requireNonNull(secret, "secret");

        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest((payload + secret).getBytes(StandardCharsets.UTF_8));

            return Strings.toHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(HASH_ALGORITHM + " is required by every Java platform.", e);
        }
    }

    /**
     * Decodes a payload without verifying its signature.
     *
     * @param payload The raw request body.
     * @return The decoded event.
     * @throws DvNetInvalidWebhookException If the payload is not valid JSON or matches none of the
     *                                      known webhook shapes.
     */
    public WebhookEvent parse(final String payload) throws DvNetInvalidWebhookException {
        try {
            return webhookMapper.map(jsonMapper.readTree(payload));
        } catch (DvNetInvalidResponseDataException e) {
            throw new DvNetInvalidWebhookException("The webhook payload is not valid JSON.", e);
        }
    }

    /**
     * Verifies a payload and decodes it, which is the recommended way to handle an incoming
     * webhook.
     *
     * @param payload   The raw request body, exactly as received.
     * @param signature The value of the {@value #SIGNATURE_HEADER} header.
     * @param secret    The webhook secret found in the merchant settings.
     * @return The decoded event.
     * @throws DvNetException If the signature does not match, or if the payload cannot be decoded.
     */
    public WebhookEvent constructEvent(final String payload, final String signature, final String secret)
            throws DvNetException {
        if (!verify(payload, signature, secret)) {
            throw new DvNetInvalidWebhookException("The webhook signature does not match the payload.");
        }

        return parse(payload);
    }
}
