package net.dv.client.services.util;

/**
 * The API payloads used across the service tests, kept in one place so that a change in the wire
 * format only has to be reflected once.
 *
 * <p>Every payload is a verbatim sample of what the DV.net merchant API returns, including the
 * {@code {"code", "data", "message"}} envelope.</p>
 */
public final class Payloads {

    private Payloads() {
    }

    /**
     * Wraps a payload in the standard API response envelope.
     *
     * @param data The JSON value of the {@code data} member.
     * @return The enveloped response body.
     */
    public static String envelope(final String data) {
        return "{\"code\":200,\"message\":\"ok\",\"data\":" + data + "}";
    }

    /**
     * Builds an API error body.
     *
     * @param code    The status code echoed in the body.
     * @param field   The field the error refers to.
     * @param message The error message.
     * @return The error response body.
     */
    public static String error(final int code, final String field, final String message) {
        return "{\"code\":" + code + ",\"errors\":[{\"field\":\"" + field + "\",\"message\":\"" + message + "\"}]}";
    }

    /** The response of {@code GET /api/v1/external/store/currencies}. */
    public static final String CURRENCIES = envelope(""
            + "[\n"
            + "  {\n"
            + "    \"id\": \"USDT.Tron\",\n"
            + "    \"code\": \"USDT\",\n"
            + "    \"name\": \"Tether USD\",\n"
            + "    \"precision\": 6,\n"
            + "    \"is_fiat\": false,\n"
            + "    \"blockchain\": \"tron\",\n"
            + "    \"contract_address\": \"TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t\",\n"
            + "    \"withdrawal_min_balance\": \"1.5\",\n"
            + "    \"has_balance\": true,\n"
            + "    \"status\": true,\n"
            + "    \"min_confirmation\": 19,\n"
            + "    \"icon\": {\n"
            + "      \"icon_128\": \"https://cdn.dv.net/usdt_128.png\",\n"
            + "      \"icon_512\": \"https://cdn.dv.net/usdt_512.png\",\n"
            + "      \"icon_svg\": \"https://cdn.dv.net/usdt.svg\"\n"
            + "    },\n"
            + "    \"blockchain_icon\": {\n"
            + "      \"icon_128\": \"https://cdn.dv.net/tron_128.png\",\n"
            + "      \"icon_512\": \"https://cdn.dv.net/tron_512.png\",\n"
            + "      \"icon_svg\": \"https://cdn.dv.net/tron.svg\"\n"
            + "    },\n"
            + "    \"explorer_link\": \"https://tronscan.org/#/transaction/\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"id\": \"BTC.Bitcoin\",\n"
            + "    \"code\": \"BTC\",\n"
            + "    \"name\": \"Bitcoin\",\n"
            + "    \"precision\": 8,\n"
            + "    \"is_fiat\": false,\n"
            + "    \"blockchain\": \"bitcoin\",\n"
            + "    \"contract_address\": \"\",\n"
            + "    \"withdrawal_min_balance\": \"0.0001\",\n"
            + "    \"has_balance\": false,\n"
            + "    \"status\": true,\n"
            + "    \"min_confirmation\": 2,\n"
            + "    \"icon\": null,\n"
            + "    \"blockchain_icon\": null,\n"
            + "    \"explorer_link\": \"https://mempool.space/tx/\"\n"
            + "  }\n"
            + "]\n");

    /** The response of {@code GET /api/v1/external/store/currencies-extended}. */
    public static final String EXTENDED_CURRENCIES = envelope(""
            + "{\n"
            + "  \"tokens\": [\n"
            + "    {\n"
            + "      \"name\": \"USDT\",\n"
            + "      \"icon\": {\"icon_128\": \"u128\", \"icon_512\": \"u512\", \"icon_svg\": \"usvg\"},\n"
            + "      \"currencies\": [\"USDT.Tron\", \"USDT.Ethereum\"],\n"
            + "      \"blockchains\": [\"tron\", \"ethereum\"]\n"
            + "    }\n"
            + "  ],\n"
            + "  \"blockchains\": [\n"
            + "    {\n"
            + "      \"name\": \"tron\",\n"
            + "      \"icon\": {\"icon_128\": \"t128\", \"icon_512\": \"t512\", \"icon_svg\": \"tsvg\"},\n"
            + "      \"currencies\": [\"TRX.Tron\", \"USDT.Tron\"],\n"
            + "      \"tokens\": [\"USDT\"]\n"
            + "    }\n"
            + "  ],\n"
            + "  \"currencies\": [\n"
            + "    {\n"
            + "      \"id\": \"USDT.Tron\",\n"
            + "      \"code\": \"USDT\",\n"
            + "      \"name\": \"Tether USD\",\n"
            + "      \"blockchain\": \"tron\",\n"
            + "      \"contract_address\": \"TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t\",\n"
            + "      \"has_balance\": true,\n"
            + "      \"min_confirmation\": 19,\n"
            + "      \"icon\": {\"icon_128\": \"c128\", \"icon_512\": \"c512\", \"icon_svg\": \"csvg\"},\n"
            + "      \"token_icon\": {\"icon_128\": \"tk128\", \"icon_512\": \"tk512\", \"icon_svg\": \"tksvg\"},\n"
            + "      \"blockchain_icon\": {\"icon_128\": \"b128\", \"icon_512\": \"b512\", \"icon_svg\": \"bsvg\"},\n"
            + "      \"explorer_link\": \"https://tronscan.org/#/transaction/\"\n"
            + "    }\n"
            + "  ]\n"
            + "}\n");

    /** The response of {@code GET /api/v1/external/store/currencies/rate}. */
    public static final String CURRENCY_RATES = envelope(""
            + "[\n"
            + "  {\"code\": \"USDT\", \"rate_source\": \"binance\", \"rate\": \"1.0102\", \"original_rate\": \"1.0002\"},\n"
            + "  {\"code\": \"BTC\", \"rate_source\": \"binance\", \"rate\": \"64872.42\", \"original_rate\": \"64230.12\"}\n"
            + "]\n");

    /** The response of {@code POST /api/v1/external/wallet}. */
    public static final String WALLET = envelope(""
            + "{\n"
            + "  \"id\": \"9a07b545-f99e-4f51-bf37-a84fcbe4df4d\",\n"
            + "  \"store_id\": \"0f4d1d2a-0b2f-4a24-9c02-1a3c2c1d4e5f\",\n"
            + "  \"store_external_id\": \"customer-42\",\n"
            + "  \"pay_url\": \"https://pay.dv.net/0f4d1d2a/customer-42\",\n"
            + "  \"amount_usd\": \"10.50\",\n"
            + "  \"created_at\": \"2025-03-17T12:57:52.449795Z\",\n"
            + "  \"updated_at\": \"2025-03-17T12:57:52.449795Z\",\n"
            + "  \"rates\": {\"USDT.Tron\": \"1.0002\", \"BTC.Bitcoin\": \"64230.12\"},\n"
            + "  \"address\": [\n"
            + "    {\n"
            + "      \"id\": \"4bbc91fd-a950-4fd0-83f3-9f1c09a6b54f\",\n"
            + "      \"wallet_id\": \"9a07b545-f99e-4f51-bf37-a84fcbe4df4d\",\n"
            + "      \"user_id\": \"8a279ec7-31fb-41f2-a2a5-3d066b77bd8a\",\n"
            + "      \"currency_id\": \"USDT.Tron\",\n"
            + "      \"blockchain\": \"tron\",\n"
            + "      \"address\": \"TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV\",\n"
            + "      \"dirty\": false,\n"
            + "      \"created_at\": \"2025-03-17T12:57:52.449795Z\",\n"
            + "      \"updated_at\": \"2025-03-17T12:57:52.449795Z\",\n"
            + "      \"deleted_at\": \"0001-01-01T00:00:00Z\"\n"
            + "    }\n"
            + "  ]\n"
            + "}\n");

    /** The response of {@code GET /api/v1/external/wallet/balance/hot}. */
    public static final String HOT_WALLET_BALANCES = envelope(""
            + "[\n"
            + "  {\n"
            + "    \"balance\": \"1520.4\",\n"
            + "    \"balance_usd\": \"1520.7\",\n"
            + "    \"count\": 12,\n"
            + "    \"count_with_balance\": 3,\n"
            + "    \"currency\": {\n"
            + "      \"id\": \"USDT.Tron\",\n"
            + "      \"code\": \"USDT\",\n"
            + "      \"name\": \"Tether USD\",\n"
            + "      \"blockchain\": \"tron\",\n"
            + "      \"sort_order\": 1\n"
            + "    }\n"
            + "  }\n"
            + "]\n");

    /** The response of {@code GET /api/v1/external/exchange-balances}. */
    public static final String EXCHANGE_BALANCES = envelope(""
            + "{\n"
            + "  \"total_usd\": \"3120.55\",\n"
            + "  \"balances\": [\n"
            + "    {\"currency\": \"USDT\", \"amount\": \"3000.10\", \"amount_usd\": \"3000.70\"},\n"
            + "    {\"currency\": \"TRX\", \"amount\": \"5000\", \"amount_usd\": \"119.85\"}\n"
            + "  ]\n"
            + "}\n");

    /** The response of {@code GET /api/v1/external/processing-wallet-balances}. */
    public static final String PROCESSING_WALLET_BALANCES = envelope(""
            + "[\n"
            + "  {\n"
            + "    \"address\": \"TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV\",\n"
            + "    \"blockchain\": \"tron\",\n"
            + "    \"currency\": {\"id\": \"TRX.Tron\", \"code\": \"TRX\", \"name\": \"Tron\", \"blockchain\": \"tron\"},\n"
            + "    \"balance\": {\"native_token\": \"1250.5\", \"native_token_usd\": \"299.6\"},\n"
            + "    \"assets\": [\n"
            + "      {\"identity\": \"USDT\", \"currency_id\": \"USDT.Tron\", \"amount\": \"800.25\", \"amount_usd\": \"800.41\"}\n"
            + "    ],\n"
            + "    \"additional_data\": {\n"
            + "      \"tron_data\": {\n"
            + "        \"tron_transfer_data\": {\"max_transfers_trc20\": \"42\", \"max_transfers_native\": \"310\"},\n"
            + "        \"stacked_trx\": \"1000\",\n"
            + "        \"stacked_energy\": \"65000\",\n"
            + "        \"stacked_energy_trx\": \"800\",\n"
            + "        \"stacked_bandwidth\": \"2000\",\n"
            + "        \"stacked_bandwidth_trx\": \"200\",\n"
            + "        \"total_energy\": \"70000\",\n"
            + "        \"total_used_energy\": \"5000\",\n"
            + "        \"available_energy_for_use\": \"65000\",\n"
            + "        \"total_bandwidth\": \"2500\",\n"
            + "        \"total_used_bandwidth\": \"500\",\n"
            + "        \"available_bandwidth_for_use\": \"2000\"\n"
            + "      }\n"
            + "    }\n"
            + "  },\n"
            + "  {\n"
            + "    \"address\": \"0x52908400098527886E0F7030069857D2E4169EE7\",\n"
            + "    \"blockchain\": \"arbitrum\",\n"
            + "    \"currency\": {\"id\": \"ETH.Arbitrum\", \"code\": \"ETH\", \"name\": \"Ethereum\", \"blockchain\": \"arbitrum\"},\n"
            + "    \"assets\": [],\n"
            + "    \"additional_data\": {\n"
            + "      \"evm_data\": {\n"
            + "        \"suggested_gas_price\": \"10000000\",\n"
            + "        \"max_transfers_erc20\": \"120\",\n"
            + "        \"max_transfers_native\": \"300\",\n"
            + "        \"cost_per_erc20\": \"650000000000\",\n"
            + "        \"cost_per_native\": \"210000000000\",\n"
            + "        \"is_l2\": true,\n"
            + "        \"l1_data_fee_estimate\": \"30000000000\"\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "]\n");

    /** The response of {@code POST /api/v1/external/withdrawal-from-processing}. */
    public static final String WITHDRAWAL = envelope(""
            + "{\n"
            + "  \"id\": \"408a97b1-d1e3-423e-9c8b-5ae4cd902f7f\",\n"
            + "  \"transfer_id\": null,\n"
            + "  \"store_id\": \"0f4d1d2a-0b2f-4a24-9c02-1a3c2c1d4e5f\",\n"
            + "  \"currency_id\": \"USDT.Tron\",\n"
            + "  \"address_from\": \"TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV\",\n"
            + "  \"address_to\": \"TWd4WrZ9wn84f5x1hZhL4DHvk738ns5jwb\",\n"
            + "  \"amount\": \"100\",\n"
            + "  \"amount_usd\": \"100.02\",\n"
            + "  \"created_at\": \"2025-09-23T12:27:08.166963191Z\"\n"
            + "}\n");

    /** The response of {@code GET /api/v1/external/withdrawal-from-processing/{id}}. */
    public static final String WITHDRAWAL_STATUS = envelope(""
            + "{\n"
            + "  \"tx_hash\": \"0f8b2c8e5d5f4a1e\",\n"
            + "  \"store_id\": \"0f4d1d2a-0b2f-4a24-9c02-1a3c2c1d4e5f\",\n"
            + "  \"currency_id\": \"USDT.Tron\",\n"
            + "  \"address_from\": \"TXk9ArDLcnbAd7BsxHhY5nCcJPBQr9dyMV\",\n"
            + "  \"address_to\": \"TWd4WrZ9wn84f5x1hZhL4DHvk738ns5jwb\",\n"
            + "  \"amount\": \"100\",\n"
            + "  \"amount_usd\": \"100.02\",\n"
            + "  \"created_at\": \"2025-09-23T12:27:08.166963191Z\",\n"
            + "  \"transfer\": {\n"
            + "    \"kind\": \"transfer\",\n"
            + "    \"stage\": \"sending\",\n"
            + "    \"status\": \"completed\",\n"
            + "    \"message\": \"\"\n"
            + "  }\n"
            + "}\n");

    /** The response of {@code GET /api/v1/external/transactions/unconfirmed/transfer}. */
    public static final String UNCONFIRMED_TRANSACTIONS = envelope(""
            + "[\n"
            + "  {\n"
            + "    \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n"
            + "    \"tx_hash\": \"2be41b0cad76bc5699c3da5d5a1d390f\",\n"
            + "    \"bc_uniq_key\": \"0\",\n"
            + "    \"type\": \"deposit\",\n"
            + "    \"blockchain\": \"litecoin\",\n"
            + "    \"currency_id\": \"LTC.Litecoin\",\n"
            + "    \"from_address\": \"ltc1qsender\",\n"
            + "    \"to_address\": \"ltc1qreceiver\",\n"
            + "    \"amount\": \"0.02552778\",\n"
            + "    \"amount_usd\": \"2.395\",\n"
            + "    \"network_created_at\": \"2025-11-07T06:10:49.104Z\",\n"
            + "    \"created_at\": \"2025-11-07T06:10:49.104Z\",\n"
            + "    \"updated_at\": \"2025-11-07T06:10:49.104Z\"\n"
            + "  }\n"
            + "]\n");
}
