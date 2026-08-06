package net.dv.client.services.transactions;

import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.mapper.JsonMapper;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.core.net.RequestOptions;
import net.dv.client.core.service.BaseService;
import net.dv.client.services.transactions.model.UnconfirmedTransaction;

import java.util.List;

/**
 * The transfers the merchant has seen but not credited yet.
 */
public final class Transactions extends BaseService {

    private static final String UNCONFIRMED_TRANSFERS_PATH = "/api/v1/external/transactions/unconfirmed/transfer";

    /**
     * Constructs the service.
     *
     * @param transport The transport executing the requests.
     */
    public Transactions(final HttpTransport transport) {
        super(transport);
    }

    /**
     * Constructs the service with an explicit mapper.
     *
     * @param transport  The transport executing the requests.
     * @param jsonMapper The mapper decoding the responses.
     */
    public Transactions(final HttpTransport transport, final JsonMapper jsonMapper) {
        super(transport, jsonMapper);
    }

    /**
     * Lists the incoming transfers waiting for blockchain confirmations.
     *
     * @return The unconfirmed transfers.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<UnconfirmedTransaction> listUnconfirmed() throws DvNetException {
        return listUnconfirmed(null);
    }

    /**
     * Lists the incoming transfers waiting for blockchain confirmations.
     *
     * @param options The per request overrides, may be {@code null}.
     * @return The unconfirmed transfers.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    public List<UnconfirmedTransaction> listUnconfirmed(final RequestOptions options) throws DvNetException {
        HttpRequest request = HttpRequest.builder(HttpMethod.GET, UNCONFIRMED_TRANSFERS_PATH)
                .options(options)
                .build();

        return sendForList(request, UnconfirmedTransaction.class);
    }
}
