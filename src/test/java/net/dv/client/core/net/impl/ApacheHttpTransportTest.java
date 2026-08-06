package net.dv.client.core.net.impl;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.dv.client.core.exception.DvNetInvalidRequestException;
import net.dv.client.core.exception.DvNetNetworkException;
import net.dv.client.core.exception.DvNetServerException;
import net.dv.client.core.exception.DvNetUndefinedApiKeyException;
import net.dv.client.core.exception.DvNetUndefinedHostException;
import net.dv.client.core.net.HttpMethod;
import net.dv.client.core.net.HttpRequest;
import net.dv.client.core.net.HttpResponse;
import net.dv.client.core.net.RequestOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercises the transport against a real HTTP server, which is the only way to assert what actually
 * goes over the wire: headers, query string, body and status code handling.
 */
class ApacheHttpTransportTest {

    private HttpServer server;
    private String host;
    private ApacheHttpTransport transport;

    private final AtomicReference<HttpExchange> lastExchange = new AtomicReference<>();
    private final AtomicReference<String> lastBody = new AtomicReference<>();

    private int responseStatus = 200;
    private String responseBody = "{\"data\":{}}";

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            lastExchange.set(exchange);

            try (InputStream body = exchange.getRequestBody()) {
                lastBody.set(readFully(body));
            }

            byte[] payload = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(responseStatus, payload.length);

            try (OutputStream out = exchange.getResponseBody()) {
                out.write(payload);
            }
        });
        server.start();

        host = "http://127.0.0.1:" + server.getAddress().getPort();
        transport = new ApacheHttpTransport(host, "store-api-key");
    }

    @AfterEach
    void tearDown() {
        transport.close();
        server.stop(0);
    }

    private static String readFully(final InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[1024];

        for (int read = stream.read(chunk); read > 0; read = stream.read(chunk)) {
            buffer.write(chunk, 0, read);
        }

        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    @Test
    void sendsTheApiKeyAndTheSdkUserAgent() throws Exception {
        transport.execute(HttpRequest.builder(HttpMethod.GET, "/api/v1/external/store/currencies").build());

        Headers headers = lastExchange.get().getRequestHeaders();
        assertEquals("store-api-key", headers.getFirst(ApacheHttpTransport.API_KEY_HEADER));
        assertEquals("application/json", headers.getFirst("Accept"));
        assertTrue(headers.getFirst("User-Agent").startsWith("dv-net-java/"));
    }

    @Test
    void buildsTheQueryStringFromTheRequestParameters() throws Exception {
        transport.execute(HttpRequest.builder(HttpMethod.GET, "/api/v1/external/processing-wallet-balances")
                .query("blockchains", "tron,ethereum")
                .query("currencies", "USDT.Tron")
                .build());

        String query = lastExchange.get().getRequestURI().getQuery();
        assertEquals("blockchains=tron,ethereum&currencies=USDT.Tron", query);
    }

    @Test
    void postsTheBodyAsJson() throws Exception {
        transport.execute(HttpRequest.builder(HttpMethod.POST, "/api/v1/external/wallet")
                .body("{\"store_external_id\":\"customer-42\"}")
                .build());

        assertEquals("POST", lastExchange.get().getRequestMethod());
        assertEquals("{\"store_external_id\":\"customer-42\"}", lastBody.get());
        assertTrue(lastExchange.get().getRequestHeaders().getFirst("Content-Type").startsWith("application/json"));
    }

    @Test
    void returnsTheStatusCodeAndBodyOfASuccessfulResponse() throws Exception {
        responseBody = "{\"data\":{\"code\":\"USDT\"}}";

        HttpResponse response = transport.execute(HttpRequest.builder(HttpMethod.GET, "/ping").build());

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"data\":{\"code\":\"USDT\"}}", response.getBody());
        assertTrue(response.isSuccessful());
    }

    @Test
    void raisesAnInvalidRequestExceptionOnAClientError() {
        responseStatus = 422;
        responseBody = "{\"code\":422,\"errors\":[{\"field\":\"amount\",\"message\":\"must be positive\"}]}";

        DvNetInvalidRequestException exception = assertThrows(DvNetInvalidRequestException.class,
                () -> transport.execute(HttpRequest.builder(HttpMethod.POST, "/api/v1/external/wallet").build()));

        assertEquals(422, exception.getStatusCode());
        assertEquals("must be positive", exception.getErrors().get(0).getMessage());
    }

    @Test
    void raisesAServerExceptionOnAServerError() {
        responseStatus = 503;
        responseBody = "{\"code\":503,\"message\":\"service unavailable\"}";

        DvNetServerException exception = assertThrows(DvNetServerException.class,
                () -> transport.execute(HttpRequest.builder(HttpMethod.GET, "/ping").build()));

        assertEquals(503, exception.getStatusCode());
    }

    @Test
    void appliesTheApiKeyAndHeaderOverridesOfTheRequestOptions() throws Exception {
        RequestOptions options = RequestOptions.builder()
                .apiKey("another-store-key")
                .header("X-Correlation-Id", "abc-123")
                .build();

        transport.execute(HttpRequest.builder(HttpMethod.GET, "/ping").options(options).build());

        Headers headers = lastExchange.get().getRequestHeaders();
        assertEquals("another-store-key", headers.getFirst(ApacheHttpTransport.API_KEY_HEADER));
        assertEquals("abc-123", headers.getFirst("X-Correlation-Id"));
    }

    @Test
    void appliesTheHostOverrideOfTheRequestOptions() throws Exception {
        ApacheHttpTransport hostless = new ApacheHttpTransport(null, "store-api-key");

        try {
            HttpResponse response = hostless.execute(HttpRequest.builder(HttpMethod.GET, "/ping")
                    .options(RequestOptions.builder().host(host).build())
                    .build());

            assertNotNull(response.getBody());
        } finally {
            hostless.close();
        }
    }

    @Test
    void toleratesATrailingSlashOnTheHost() throws Exception {
        ApacheHttpTransport withSlash = new ApacheHttpTransport(host + "/", "store-api-key");

        try {
            withSlash.execute(HttpRequest.builder(HttpMethod.GET, "/ping").build());

            assertEquals("/ping", lastExchange.get().getRequestURI().getPath());
        } finally {
            withSlash.close();
        }
    }

    @Test
    void refusesToSendWithoutAHost() {
        ApacheHttpTransport hostless = new ApacheHttpTransport(null, "store-api-key");

        try {
            assertThrows(DvNetUndefinedHostException.class,
                    () -> hostless.execute(HttpRequest.builder(HttpMethod.GET, "/ping").build()));
        } finally {
            hostless.close();
        }
    }

    @Test
    void refusesToSendWithoutAnApiKey() {
        ApacheHttpTransport keyless = new ApacheHttpTransport(host, "  ");

        try {
            assertThrows(DvNetUndefinedApiKeyException.class,
                    () -> keyless.execute(HttpRequest.builder(HttpMethod.GET, "/ping").build()));
        } finally {
            keyless.close();
        }
    }

    @Test
    void raisesANetworkExceptionWhenTheHostIsUnreachable() {
        ApacheHttpTransport unreachable = new ApacheHttpTransport("http://127.0.0.1:1", "store-api-key");

        try {
            assertThrows(DvNetNetworkException.class,
                    () -> unreachable.execute(HttpRequest.builder(HttpMethod.GET, "/ping").build()));
        } finally {
            unreachable.close();
        }
    }
}
