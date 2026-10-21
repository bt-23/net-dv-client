package net.dv.client.core.net;

import net.dv.client.core.helper.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A transport agnostic description of a call to the DV.net merchant API.
 */
public final class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, List<String>> queryParameters;
    private final String body;
    private final RequestOptions options;

    private HttpRequest(final Builder builder) {
        Map<String, List<String>> parameters = new LinkedHashMap<String, List<String>>();
        for (Map.Entry<String, List<String>> parameter : builder.queryParameters.entrySet()) {
            parameters.put(parameter.getKey(),
                    Collections.unmodifiableList(new ArrayList<String>(parameter.getValue())));
        }

        this.method = builder.method;
        this.path = builder.path;
        this.queryParameters = Collections.unmodifiableMap(parameters);
        this.body = builder.body;
        this.options = builder.options;
    }

    /**
     * Returns the HTTP method of the request.
     *
     * @return The HTTP method.
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * Returns the path of the request, relative to the merchant API host.
     *
     * @return The request path, always starting with a slash.
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the query parameters of the request, in insertion order. A parameter holding several
     * values is sent repeated, {@code name=a&name=b}.
     *
     * @return An unmodifiable map of query parameters, empty when none were added.
     */
    public Map<String, List<String>> getQueryParameters() {
        return queryParameters;
    }

    /**
     * Returns the first value of a query parameter.
     *
     * @param name The parameter name.
     * @return The first value, or {@code null} when the parameter is absent.
     */
    public String getQueryParameter(final String name) {
        List<String> values = queryParameters.get(name);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    /**
     * Returns the JSON body of the request.
     *
     * @return The request body, or {@code null} when the request carries none.
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the per request overrides.
     *
     * @return The request options, or {@code null} when the client wide settings apply.
     */
    public RequestOptions getOptions() {
        return options;
    }

    /**
     * Creates a new builder for the given method and path.
     *
     * @param method The HTTP method.
     * @param path   The path relative to the merchant API host.
     * @return A new builder instance.
     */
    public static Builder builder(final HttpMethod method, final String path) {
        return new Builder(method, path);
    }

    /**
     * Builder for {@link HttpRequest}.
     */
    public static final class Builder {

        private final HttpMethod method;
        private final String path;
        private final Map<String, List<String>> queryParameters = new LinkedHashMap<String, List<String>>();
        private String body;
        private RequestOptions options;

        private Builder(final HttpMethod method, final String path) {
            this.method = Objects.requireNonNull(method, "method");
            this.path = Objects.requireNonNull(path, "path");
        }

        /**
         * Adds a query parameter, ignoring {@code null} and blank values.
         *
         * @param name  The parameter name.
         * @param value The parameter value.
         * @return The builder instance.
         */
        public Builder query(final String name, final String value) {
            if (Strings.isNotBlank(value)) {
                List<String> values = queryParameters.get(name);
                if (values == null) {
                    values = new ArrayList<String>();
                    queryParameters.put(name, values);
                }
                values.add(value);
            }
            return this;
        }

        /**
         * Adds a query parameter once per value, ignoring {@code null} and blank values.
         *
         * <p>The merchant API binds list parameters from repeated keys only; a comma separated
         * value would be read as one single element.</p>
         *
         * @param name   The parameter name.
         * @param values The parameter values, may be {@code null}.
         * @return The builder instance.
         */
        public Builder query(final String name, final Collection<String> values) {
            if (values != null) {
                for (String value : values) {
                    query(name, value);
                }
            }
            return this;
        }

        /**
         * Sets the JSON body of the request.
         *
         * @param body The request body.
         * @return The builder instance.
         */
        public Builder body(final String body) {
            this.body = body;
            return this;
        }

        /**
         * Sets the per request overrides.
         *
         * @param options The request options.
         * @return The builder instance.
         */
        public Builder options(final RequestOptions options) {
            this.options = options;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return A new {@link HttpRequest} instance.
         */
        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
