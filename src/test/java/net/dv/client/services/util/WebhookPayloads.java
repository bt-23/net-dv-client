package net.dv.client.services.util;

/**
 * The webhook documents DV.net delivers, reproduced from the payload builders of the dv-merchant
 * server rather than from the published guide, which shows offsets and members the server never
 * sends. The server marshals Go maps, so the keys come out sorted, the body is compact, and every
 * timestamp is a Postgres {@code timestamp} without offset.
 */
public final class WebhookPayloads {

    private WebhookPayloads() {
    }

    /** A payment that reached the required number of confirmations. */
    public static final String PAYMENT_RECEIVED = ""
            + "{\"amount\":\"2.395\","
            + "\"created_at\":\"2025-03-17T12:57:52.449795\","
            + "\"paid_at\":\"2025-03-17T12:57:19\","
            + "\"status\":\"completed\","
            + "\"transactions\":{"
            + "\"amount\":\"0.02552778\","
            + "\"amount_usd\":\"2.395\","
            + "\"bc_uniq_key\":\"0\","
            + "\"blockchain\":\"litecoin\","
            + "\"created_at\":\"2025-03-17T12:57:52.449795\","
            + "\"currency\":\"LTC\","
            + "\"currency_id\":\"LTC.Litecoin\","
            + "\"tx_hash\":\"2be41b0cad76bc5699c3da5d5a1d390f9fb4038e5bfe49aec3b675f9dd4515fd\","
            + "\"tx_id\":\"4bbc91fd-a950-4fd0-83f3-9f1c09a6b54f\"},"
            + "\"type\":\"PaymentReceived\","
            + "\"wallet\":{"
            + "\"id\":\"9a07b545-f99e-4f51-bf37-a84fcbe4df4d\","
            + "\"store_external_id\":\"1\"}}";

    /**
     * A payment to an address that is not tied to a customer wallet: the wallet id is {@code null},
     * the external id is empty and the chain reported no unique key.
     */
    public static final String PAYMENT_RECEIVED_WITHOUT_WALLET = ""
            + "{\"amount\":\"15\","
            + "\"created_at\":\"2025-03-17T12:57:52.449795\","
            + "\"paid_at\":\"2025-03-17T12:57:19\","
            + "\"status\":\"completed\","
            + "\"transactions\":{"
            + "\"amount\":\"15\","
            + "\"amount_usd\":\"15\","
            + "\"bc_uniq_key\":null,"
            + "\"blockchain\":\"tron\","
            + "\"created_at\":\"2025-03-17T12:57:52.449795\","
            + "\"currency\":\"USDT\","
            + "\"currency_id\":\"USDT.Tron\","
            + "\"tx_hash\":\"9c1e2a\","
            + "\"tx_id\":\"0c1f5e86-46f5-4f5b-9b0a-0d8c2f1f6a11\"},"
            + "\"type\":\"PaymentReceived\","
            + "\"wallet\":{"
            + "\"id\":null,"
            + "\"store_external_id\":\"\"}}";

    /** A payment sitting in the mempool, every member prefixed with {@code unconfirmed_}. */
    public static final String PAYMENT_NOT_CONFIRMED = ""
            + "{\"unconfirmed_amount\":\"1000000000000\","
            + "\"unconfirmed_created_at\":\"2025-10-06T12:39:39.457399475\","
            + "\"unconfirmed_paid_at\":\"2025-10-06T12:39:39.457399475\","
            + "\"unconfirmed_status\":\"completed\","
            + "\"unconfirmed_transactions\":{"
            + "\"unconfirmed_amount\":\"1000000000000\","
            + "\"unconfirmed_amount_usd\":\"1000000000000\","
            + "\"unconfirmed_bc_uniq_key\":\"bc_uniq_key_example\","
            + "\"unconfirmed_blockchain\":\"bitcoin\","
            + "\"unconfirmed_created_at\":\"2025-10-06T12:39:39.457399475\","
            + "\"unconfirmed_currency\":\"BTC\","
            + "\"unconfirmed_currency_id\":\"BTC.Bitcoin\","
            + "\"unconfirmed_tx_hash\":\"tx_hash_example\","
            + "\"unconfirmed_tx_id\":\"165d8dd3-0d9b-4144-979e-23f593f48cdf\"},"
            + "\"unconfirmed_type\":\"PaymentNotConfirmed\","
            + "\"unconfirmed_wallet\":{"
            + "\"unconfirmed_id\":\"5f185a18-a8d5-469d-a853-7fbcb048635b\","
            + "\"unconfirmed_store_external_id\":\"store_external_example\"}}";

    /**
     * A payment held back by the AML screening. It reaches the endpoints subscribed to
     * {@code PaymentNotConfirmed}, yet its members carry no prefix.
     */
    public static final String PAYMENT_AML_BLOCKED = ""
            + "{\"aml_check\":{"
            + "\"created_at\":\"2025-10-06T12:40:01.120034\","
            + "\"id\":\"e2b8a4f4-5b0c-4a49-8f2e-7b3c3c9d1a20\","
            + "\"risk_level\":\"high\","
            + "\"score\":\"87.5\","
            + "\"status\":\"success\","
            + "\"updated_at\":\"2025-10-06T12:40:05.5\"},"
            + "\"amount\":\"250\","
            + "\"created_at\":\"2025-10-06T12:39:39.457399\","
            + "\"paid_at\":\"2025-10-06T12:39:30\","
            + "\"status\":\"completed\","
            + "\"transactions\":{"
            + "\"amount\":\"250\","
            + "\"amount_usd\":\"250\","
            + "\"bc_uniq_key\":\"0\","
            + "\"blockchain\":\"tron\","
            + "\"created_at\":\"2025-10-06T12:39:39.457399\","
            + "\"currency\":\"USDT\","
            + "\"currency_id\":\"USDT.Tron\","
            + "\"tx_hash\":\"aml_tx_hash\","
            + "\"tx_id\":\"7d0b1a52-1f0e-45a5-8d6b-2d6f41f0b8c3\"},"
            + "\"type\":\"PaymentAMLBlocked\","
            + "\"wallet\":{"
            + "\"id\":\"5f185a18-a8d5-469d-a853-7fbcb048635b\","
            + "\"store_external_id\":\"customer-42\"}}";

    /**
     * A withdrawal from a processing wallet that reached the recipient. Unlike the guide claims, it
     * carries {@code withdrawal_id} and neither {@code status} nor {@code wallet}.
     */
    public static final String WITHDRAWAL = ""
            + "{\"amount\":\"100\","
            + "\"created_at\":\"2025-09-23T09:27:08.166963\","
            + "\"paid_at\":\"2025-09-23T09:27:01\","
            + "\"transactions\":{"
            + "\"amount\":\"100\","
            + "\"amount_usd\":\"100\","
            + "\"bc_uniq_key\":\"bc_uniq_key_example\","
            + "\"blockchain\":\"bitcoin\","
            + "\"created_at\":\"2025-09-23T09:27:08.166963\","
            + "\"currency\":\"BTC\","
            + "\"currency_id\":\"BTC.Bitcoin\","
            + "\"tx_hash\":\"tx_hash_example\","
            + "\"tx_id\":\"408a97b1-d1e3-423e-9c8b-5ae4cd902f7f\"},"
            + "\"type\":\"WithdrawalFromProcessingReceived\","
            + "\"withdrawal_id\":\"c3a4a8a8-6f4e-4d0e-9a59-5a9b1f6f7e21\"}";

    /** A document that is valid JSON but announces no known webhook type. */
    public static final String UNKNOWN_SHAPE = "{\"foo\":\"bar\"}";
}
