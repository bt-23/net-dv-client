package net.dv.client.services.withdrawals;

import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.mapper.JsonMapper;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.core.net.RequestOptions;
import net.dv.client.core.service.BaseService;
import net.dv.client.services.withdrawals.model.CreateWithdrawalOptions;
import net.dv.client.services.withdrawals.model.Withdrawal;
import net.dv.client.services.withdrawals.model.WithdrawalStatus;

import java.util.Objects;

/**
 * Payouts from the processing wallets of the merchant.
 */
public final class Withdrawals extends BaseService {

    private static final String WITHDRAWAL_PATH = "/api/v1/external/withdrawal-from-processing";

    /**
     * Constructs the service.
     *
     * @param transport The transport executing the requests.
     */
    public Withdrawals(final HttpTransport transport) {
        super(transport);
    }

    /**
     * Constructs the service with an explicit mapper.
     *
     * @param transport  The transport executing the requests.
     * @param jsonMapper The mapper decoding the responses.
     */
    public Withdrawals(final HttpTransport transport, final JsonMapper jsonMapper) {
        super(transport, jsonMapper);
    }

    /**
     * Queues a withdrawal from a processing wallet.
     *
     * @param options The withdrawal parameters.
     * @return The accepted withdrawal.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public Withdrawal create(final CreateWithdrawalOptions options) throws DvNetException {
        return create(options, null);
    }

    /**
     * Queues a withdrawal from a processing wallet.
     *
     * @param options        The withdrawal parameters.
     * @param requestOptions The per request overrides, may be {@code null}.
     * @return The accepted withdrawal.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public Withdrawal create(final CreateWithdrawalOptions options, final RequestOptions requestOptions)
            throws DvNetException {
        Objects.requireNonNull(options, "options");

        HttpRequest request = HttpRequest.builder(HttpMethod.POST, WITHDRAWAL_PATH)
                .body(jsonMapper.writeValue(options))
                .options(requestOptions)
                .build();

        return send(request, Withdrawal.class);
    }

    /**
     * Retrieves the current state of a withdrawal.
     *
     * @param withdrawalId The withdrawal identifier, or the {@code requestId} it was created with.
     * @return The current state of the withdrawal.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public WithdrawalStatus get(final String withdrawalId) throws DvNetException {
        return get(withdrawalId, null);
    }

    /**
     * Retrieves the current state of a withdrawal.
     *
     * @param withdrawalId The withdrawal identifier, or the {@code requestId} it was created with.
     * @param options      The per request overrides, may be {@code null}.
     * @return The current state of the withdrawal.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public WithdrawalStatus get(final String withdrawalId, final RequestOptions options) throws DvNetException {
        Objects.requireNonNull(withdrawalId, "withdrawalId");

        HttpRequest request = HttpRequest.builder(HttpMethod.GET, WITHDRAWAL_PATH + "/" + withdrawalId)
                .options(options)
                .build();

        return send(request, WithdrawalStatus.class);
    }

    /**
     * Cancels a withdrawal that has not been executed yet.
     *
     * @param withdrawalId The withdrawal identifier.
     * @throws DvNetException If the request fails, in particular when the withdrawal has already
     *                        been sent to the blockchain.
     */
    public void remove(final String withdrawalId) throws DvNetException {
        remove(withdrawalId, null);
    }

    /**
     * Cancels a withdrawal that has not been executed yet.
     *
     * @param withdrawalId The withdrawal identifier.
     * @param options      The per request overrides, may be {@code null}.
     * @throws DvNetException If the request fails, in particular when the withdrawal has already
     *                        been sent to the blockchain.
     */
    public void remove(final String withdrawalId, final RequestOptions options) throws DvNetException {
        Objects.requireNonNull(withdrawalId, "withdrawalId");

        HttpRequest request = HttpRequest.builder(HttpMethod.DELETE, WITHDRAWAL_PATH + "/" + withdrawalId)
                .options(options)
                .build();

        send(request);
    }
}
