package net.dv.client.core.service;

import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.mapper.DvNetJsonMapper;
import net.dv.client.core.mapper.JsonMapper;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpResponse;
import net.dv.client.core.net.HttpTransport;

import java.util.List;
import java.util.Objects;

/**
 * Shared plumbing for the API service classes: it holds the transport and the JSON mapper, and
 * turns a {@link HttpRequest} into a decoded model in a single step.
 */
public abstract class BaseService {

    /**
     * The transport executing the requests.
     */
    protected final HttpTransport transport;

    /**
     * The mapper decoding the responses.
     */
    protected final JsonMapper jsonMapper;

    /**
     * Constructs a service on top of the given transport, with the default JSON mapper.
     *
     * @param transport The transport executing the requests.
     */
    protected BaseService(final HttpTransport transport) {
        this(transport, new DvNetJsonMapper());
    }

    /**
     * Constructs a service on top of the given transport and mapper.
     *
     * @param transport  The transport executing the requests.
     * @param jsonMapper The mapper decoding the responses.
     */
    protected BaseService(final HttpTransport transport, final JsonMapper jsonMapper) {
        this.transport = Objects.requireNonNull(transport, "transport");
        this.jsonMapper = Objects.requireNonNull(jsonMapper, "jsonMapper");
    }

    /**
     * Returns the transport used by this service.
     *
     * @return The transport.
     */
    public HttpTransport getTransport() {
        return transport;
    }

    /**
     * Executes a request and decodes the {@code data} member of the response into the given type.
     *
     * @param request The request to execute.
     * @param type    The type to decode the payload into.
     * @param <T>     The type of the decoded payload.
     * @return The decoded payload.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    protected <T> T send(final HttpRequest request, final Class<T> type) throws DvNetException {
        HttpResponse response = transport.execute(request);
        return jsonMapper.readData(response.getBody(), type);
    }

    /**
     * Executes a request and decodes the {@code data} member of the response into a list.
     *
     * @param request     The request to execute.
     * @param elementType The type of the list elements.
     * @param <T>         The type of the list elements.
     * @return The decoded list.
     * @throws DvNetException If the request fails or the response cannot be decoded.
     */
    protected <T> List<T> sendForList(final HttpRequest request, final Class<T> elementType) throws DvNetException {
        HttpResponse response = transport.execute(request);
        return jsonMapper.readDataList(response.getBody(), elementType);
    }

    /**
     * Executes a request whose response payload is not needed.
     *
     * @param request The request to execute.
     * @throws DvNetException If the request fails.
     */
    protected void send(final HttpRequest request) throws DvNetException {
        transport.execute(request);
    }
}
