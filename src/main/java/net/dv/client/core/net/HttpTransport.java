package net.dv.client.core.net;

import net.dv.client.core.exception.DvNetException;

import java.io.Closeable;

/**
 * Executes {@link HttpRequest}s against the DV.net merchant API.
 *
 * <p>Implementations own the merchant host, the store API key and the underlying connection pool,
 * which keeps the service classes free of any transport concern. Supplying a custom implementation
 * is the supported way to plug in another HTTP stack or to stub the API in tests.</p>
 */
public interface HttpTransport extends Closeable {

    /**
     * Executes a request and returns the raw response.
     *
     * @param request The request to execute.
     * @return The raw response, always with a 2xx status code.
     * @throws net.dv.client.core.exception.DvNetUndefinedHostException   If no merchant host is set.
     * @throws net.dv.client.core.exception.DvNetUndefinedApiKeyException If no store API key is set.
     * @throws net.dv.client.core.exception.DvNetNetworkException         If the request never reached the API.
     * @throws net.dv.client.core.exception.DvNetInvalidRequestException  If the API answered with a 4xx status code.
     * @throws net.dv.client.core.exception.DvNetServerException          If the API answered with a 5xx status code.
     * @throws DvNetException                                             If the request fails for any other reason.
     */
    HttpResponse execute(HttpRequest request) throws DvNetException;

    /**
     * Releases the resources held by the transport.
     */
    @Override
    void close();
}
