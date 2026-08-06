package net.dv.client.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Exposes the SDK version, which is reported to the API through the {@code User-Agent} header.
 */
public final class SdkVersion {

    private static final String PROPERTIES_FILE = "dv-net-sdk.properties";

    private static final String VERSION;

    static {
        String version = "unknown";

        try (InputStream stream = SdkVersion.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (stream != null) {
                Properties properties = new Properties();
                properties.load(stream);
                version = properties.getProperty("version", "unknown");
            }
        } catch (IOException ignored) {
            // The version is informational only, an unreadable descriptor must not break the client.
        }

        VERSION = version;
    }

    private SdkVersion() {
    }

    /**
     * Returns the SDK version.
     *
     * @return The version string, or {@code unknown} when it could not be read.
     */
    public static String getVersion() {
        return VERSION;
    }
}
