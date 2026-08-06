package net.dv.client.core.net;

/**
 * The HTTP methods used by the DV.net merchant API.
 */
public enum HttpMethod {

    /** Retrieves a resource. */
    GET,

    /** Creates a resource or triggers an operation. */
    POST,

    /** Replaces a resource. */
    PUT,

    /** Partially updates a resource. */
    PATCH,

    /** Removes a resource. */
    DELETE
}
