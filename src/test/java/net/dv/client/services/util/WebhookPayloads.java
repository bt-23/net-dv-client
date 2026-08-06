package net.dv.client.services.util;

/**
 * The webhook documents DV.net delivers, taken verbatim from the integration guide.
 */
public final class WebhookPayloads {

    private WebhookPayloads() {
    }

    /** A payment that reached the required number of confirmations. */
    public static final String PAYMENT_RECEIVED = ""
            + "{\n"
            + "  \"amount\": \"2.395\",\n"
            + "  \"created_at\": \"2025-03-17T12:57:52.449795Z\",\n"
            + "  \"paid_at\": \"2025-03-17T12:57:19Z\",\n"
            + "  \"status\": \"completed\",\n"
            + "  \"transactions\": {\n"
            + "    \"amount\": \"0.02552778\",\n"
            + "    \"amount_usd\": \"2.395\",\n"
            + "    \"bc_uniq_key\": \"0\",\n"
            + "    \"blockchain\": \"litecoin\",\n"
            + "    \"created_at\": \"2025-03-17T12:57:52.449795Z\",\n"
            + "    \"currency\": \"LTC\",\n"
            + "    \"currency_id\": \"LTC.Litecoin\",\n"
            + "    \"tx_hash\": \"2be41b0cad76bc5699c3da5d5a1d390f9fb4038e5bfe49aec3b675f9dd4515fd\",\n"
            + "    \"tx_id\": \"4bbc91fd-a950-4fd0-83f3-9f1c09a6b54f\"\n"
            + "  },\n"
            + "  \"type\": \"PaymentReceived\",\n"
            + "  \"wallet\": {\n"
            + "    \"id\": \"9a07b545-f99e-4f51-bf37-a84fcbe4df4d\",\n"
            + "    \"store_external_id\": \"1\"\n"
            + "  }\n"
            + "}\n";

    /** A payment sitting in the mempool, every member prefixed with {@code unconfirmed_}. */
    public static final String PAYMENT_NOT_CONFIRMED = ""
            + "{\n"
            + "  \"unconfirmed_amount\": \"1000000000000\",\n"
            + "  \"unconfirmed_created_at\": \"2025-10-06T12:39:39.457399475\",\n"
            + "  \"unconfirmed_paid_at\": \"2025-10-06T12:39:39.457399475\",\n"
            + "  \"unconfirmed_status\": \"completed\",\n"
            + "  \"unconfirmed_transactions\": {\n"
            + "    \"unconfirmed_amount\": \"1000000000000\",\n"
            + "    \"unconfirmed_amount_usd\": \"1000000000000\",\n"
            + "    \"unconfirmed_bc_uniq_key\": \"bc_uniq_key_example\",\n"
            + "    \"unconfirmed_blockchain\": \"bitcoin\",\n"
            + "    \"unconfirmed_created_at\": \"2025-10-06T12:39:39.457399475\",\n"
            + "    \"unconfirmed_currency\": \"BTC\",\n"
            + "    \"unconfirmed_currency_id\": \"BTC.Bitcoin\",\n"
            + "    \"unconfirmed_tx_hash\": \"tx_hash_example\",\n"
            + "    \"unconfirmed_tx_id\": \"165d8dd3-0d9b-4144-979e-23f593f48cdf\"\n"
            + "  },\n"
            + "  \"unconfirmed_type\": \"PaymentNotConfirmed\",\n"
            + "  \"unconfirmed_wallet\": {\n"
            + "    \"unconfirmed_id\": \"5f185a18-a8d5-469d-a853-7fbcb048635b\",\n"
            + "    \"unconfirmed_store_external_id\": \"store_external_example\"\n"
            + "  }\n"
            + "}\n";

    /** A withdrawal from a processing wallet that reached the recipient. */
    public static final String WITHDRAWAL = ""
            + "{\n"
            + "  \"amount\": \"100\",\n"
            + "  \"created_at\": \"2025-09-23T12:27:08.166963191+03:00\",\n"
            + "  \"paid_at\": \"2025-09-23T12:27:08.166963191+03:00\",\n"
            + "  \"status\": \"completed\",\n"
            + "  \"transactions\": {\n"
            + "    \"amount\": \"100\",\n"
            + "    \"amount_usd\": \"100\",\n"
            + "    \"bc_uniq_key\": \"bc_uniq_key_example\",\n"
            + "    \"blockchain\": \"bitcoin\",\n"
            + "    \"created_at\": \"2025-09-23T12:27:08.166963191+03:00\",\n"
            + "    \"currency\": \"BTC\",\n"
            + "    \"currency_id\": \"BTC.Bitcoin\",\n"
            + "    \"tx_hash\": \"tx_hash_example\",\n"
            + "    \"tx_id\": \"408a97b1-d1e3-423e-9c8b-5ae4cd902f7f\"\n"
            + "  },\n"
            + "  \"type\": \"WithdrawalFromProcessingReceived\",\n"
            + "  \"wallet\": {\n"
            + "    \"id\": \"8a279ec7-31fb-41f2-a2a5-3d066b77bd8a\",\n"
            + "    \"store_external_id\": \"store_external_example\"\n"
            + "  }\n"
            + "}\n";

    /** A withdrawal announced through the legacy {@code withdrawal_id} member. */
    public static final String WITHDRAWAL_WITH_ID = ""
            + "{\n"
            + "  \"withdrawal_id\": \"408a97b1-d1e3-423e-9c8b-5ae4cd902f7f\",\n"
            + "  \"amount\": \"100\",\n"
            + "  \"created_at\": \"2025-09-23T12:27:08.166963191+03:00\",\n"
            + "  \"paid_at\": \"2025-09-23T12:27:08.166963191+03:00\",\n"
            + "  \"status\": \"completed\",\n"
            + "  \"type\": \"WithdrawalReceived\",\n"
            + "  \"transactions\": {\n"
            + "    \"amount\": \"100\",\n"
            + "    \"amount_usd\": \"100\",\n"
            + "    \"bc_uniq_key\": \"bc_uniq_key_example\",\n"
            + "    \"blockchain\": \"bitcoin\",\n"
            + "    \"created_at\": \"2025-09-23T12:27:08.166963191+03:00\",\n"
            + "    \"currency\": \"BTC\",\n"
            + "    \"currency_id\": \"BTC.Bitcoin\",\n"
            + "    \"tx_hash\": \"tx_hash_example\",\n"
            + "    \"tx_id\": \"408a97b1-d1e3-423e-9c8b-5ae4cd902f7f\"\n"
            + "  }\n"
            + "}\n";

    /** A document that is valid JSON but announces no known webhook type. */
    public static final String UNKNOWN_SHAPE = "{\"foo\":\"bar\"}";
}
