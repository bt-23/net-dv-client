package net.dv.client.core.net.impl;

import net.dv.client.core.SdkVersion;
import net.dv.client.core.exception.DvNetException;
import net.dv.client.core.exception.DvNetInvalidRequestException;
import net.dv.client.core.exception.DvNetNetworkException;
import net.dv.client.core.exception.DvNetServerException;
import net.dv.client.core.exception.DvNetUndefinedApiKeyException;
import net.dv.client.core.exception.DvNetUndefinedHostException;
import net.dv.client.core.helper.Strings;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpResponse;
import net.dv.client.core.net.HttpTransport;
import net.dv.client.core.net.RequestOptions;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * {@link HttpTransport} implementation backed by Apache HttpClient 5.
 *
 * <p>The transport owns a pooled {@link CloseableHttpClient}, so a single instance should be shared
 * for the lifetime of the application and closed on shutdown.</p>
 */
public class ApacheHttpTransport implements HttpTransport {

    /**
     * The header carrying the store API key, as expected by the merchant API.
     */
    public static final String API_KEY_HEADER = "x-api-key";

    /**
     * The {@code User-Agent} reported to the API.
     */
    public static final String USER_AGENT = "dv-net-java/" + SdkVersion.getVersion();

    private final String host;
    private final String apiKey;
    private final CloseableHttpClient httpClient;

    /**
     * Constructs a transport with a default connection pool.
     *
     * @param host   The merchant API host, for example {@code https://merchant.example.com}.
     * @param apiKey The store API key.
     */
    public ApacheHttpTransport(final String host, final String apiKey) {
        this(host, apiKey, HttpClients.createDefault());
    }

    /**
     * Constructs a transport reusing an existing HTTP client, which lets the caller configure
     * timeouts, proxies or a shared connection manager.
     *
     * @param host       The merchant API host.
     * @param apiKey     The store API key.
     * @param httpClient The HTTP client to use.
     */
    public ApacheHttpTransport(final String host, final String apiKey, final CloseableHttpClient httpClient) {
        this.host = host;
        this.apiKey = apiKey;
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponse execute(final HttpRequest request) throws DvNetException {
        RequestOptions options = request.getOptions();
        String resolvedHost = resolveHost(options);
        String resolvedApiKey = resolveApiKey(options);

        HttpUriRequestBase httpRequest = new HttpUriRequestBase(
                request.getMethod().name(), buildUri(resolvedHost, request));

        httpRequest.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpRequest.addHeader("User-Agent", USER_AGENT);
        httpRequest.addHeader(API_KEY_HEADER, resolvedApiKey);

        if (options != null) {
            for (Map.Entry<String, String> header : options.getAdditionalHeaders().entrySet()) {
                httpRequest.setHeader(header.getKey(), header.getValue());
            }
        }

        if (request.getBody() != null) {
            httpRequest.setEntity(new StringEntity(request.getBody(), ContentType.APPLICATION_JSON));
        }

        HttpResponse response;

        try {
            response = httpClient.execute(httpRequest, ApacheHttpTransport::toResponse);
        } catch (IOException e) {
            throw new DvNetNetworkException(
                    "Unable to reach the DV.net API at " + httpRequest.getRequestUri(), e);
        }

        ensureSuccessful(response);

        return response;
    }

    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (IOException ignored) {
            // Closing is best effort, a failure here must not mask the caller's own outcome.
        }
    }

    private static HttpResponse toResponse(final org.apache.hc.core5.http.ClassicHttpResponse response)
            throws IOException {
        HttpEntity entity = response.getEntity();
        String body = null;

        if (entity != null) {
            try {
                body = EntityUtils.toString(entity);
            } catch (org.apache.hc.core5.http.ParseException e) {
                throw new IOException("Unable to read the response body.", e);
            }
        }

        return new HttpResponse(response.getCode(), body);
    }

    private static void ensureSuccessful(final HttpResponse response) throws DvNetException {
        if (response.isSuccessful()) {
            return;
        }

        if (response.getStatusCode() >= 500) {
            throw new DvNetServerException(response.getStatusCode(), response.getBody());
        }

        throw new DvNetInvalidRequestException(response.getStatusCode(), response.getBody());
    }

    private String resolveHost(final RequestOptions options) throws DvNetUndefinedHostException {
        String resolved = options != null && Strings.isNotBlank(options.getHost()) ? options.getHost() : host;

        if (Strings.isBlank(resolved)) {
            throw new DvNetUndefinedHostException(
                    "Please set the merchant host on the client, or pass it in the request options.");
        }

        return resolved.endsWith("/") ? resolved.substring(0, resolved.length() - 1) : resolved;
    }

    private String resolveApiKey(final RequestOptions options) throws DvNetUndefinedApiKeyException {
        String resolved = options != null && Strings.isNotBlank(options.getApiKey()) ? options.getApiKey() : apiKey;

        if (Strings.isBlank(resolved)) {
            throw new DvNetUndefinedApiKeyException(
                    "Please set the store API key on the client, or pass it in the request options.");
        }

        return resolved;
    }

    private static URI buildUri(final String host, final HttpRequest request) throws DvNetException {
        try {
            URIBuilder builder = new URIBuilder(host + request.getPath());

            for (Map.Entry<String, String> parameter : request.getQueryParameters().entrySet()) {
                builder.addParameter(parameter.getKey(), parameter.getValue());
            }

            return builder.build();
        } catch (URISyntaxException e) {
            throw new DvNetException("Unable to build a valid URI for " + host + request.getPath(), e);
        }
    }
}
