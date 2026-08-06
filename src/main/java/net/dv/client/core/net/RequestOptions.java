package net.dv.client.core.net;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Per request overrides.
 *
 * <p>A client is normally bound to a single merchant host and store API key, but both can be
 * overridden for a single call, which is useful when one process serves several stores.</p>
 */
public final class RequestOptions {

    private final String host;
    private final String apiKey;
    private final Map<String, String> additionalHeaders;

    private RequestOptions(final Builder builder) {
        this.host = builder.host;
        this.apiKey = builder.apiKey;
        this.additionalHeaders = Collections.unmodifiableMap(new LinkedHashMap<>(builder.additionalHeaders));
    }

    /**
     * Returns the merchant API host overriding the client wide one.
     *
     * @return The host, or {@code null} when the client wide host applies.
     */
    public String getHost() {
        return host;
    }

    /**
     * Returns the store API key overriding the client wide one.
     *
     * @return The API key, or {@code null} when the client wide key applies.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Returns the extra headers to send with the request.
     *
     * @return An unmodifiable map of headers, empty when none were added.
     */
    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders;
    }

    /**
     * Creates a new builder.
     *
     * @return A new builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link RequestOptions}.
     */
    public static final class Builder {

        private String host;
        private String apiKey;
        private final Map<String, String> additionalHeaders = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * Overrides the merchant API host for this request.
         *
         * @param host The merchant API host.
         * @return The builder instance.
         */
        public Builder host(final String host) {
            this.host = host;
            return this;
        }

        /**
         * Overrides the store API key for this request.
         *
         * @param apiKey The store API key.
         * @return The builder instance.
         */
        public Builder apiKey(final String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Adds an extra header to the request.
         *
         * @param name  The header name.
         * @param value The header value.
         * @return The builder instance.
         */
        public Builder header(final String name, final String value) {
            this.additionalHeaders.put(name, value);
            return this;
        }

        /**
         * Adds several extra headers to the request.
         *
         * @param headers The headers to add.
         * @return The builder instance.
         */
        public Builder headers(final Map<String, String> headers) {
            if (headers != null) {
                this.additionalHeaders.putAll(headers);
            }
            return this;
        }

        /**
         * Builds the request options.
         *
         * @return A new {@link RequestOptions} instance.
         */
        public RequestOptions build() {
            return new RequestOptions(this);
        }
    }
}
