package net.dv.client.core.net;

/**
 * The raw outcome of a call to the DV.net merchant API.
 */
public final class HttpResponse {

    private final int statusCode;
    private final String body;

    /**
     * Constructs a response.
     *
     * @param statusCode The HTTP status code.
     * @param body       The raw response body.
     */
    public HttpResponse(final int statusCode, final String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * Returns the HTTP status code.
     *
     * @return The HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the raw response body.
     *
     * @return The response body, possibly {@code null} for an empty response.
     */
    public String getBody() {
        return body;
    }

    /**
     * Tells whether the API answered with a 2xx status code.
     *
     * @return {@code true} when the request succeeded.
     */
    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    @Override
    public String toString() {
        return "HttpResponse{statusCode=" + statusCode + ", body='" + body + "'}";
    }
}
