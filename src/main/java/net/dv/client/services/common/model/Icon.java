package net.dv.client.services.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The set of artwork URLs served for a currency, a token or a blockchain.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Icon {

    @JsonProperty("icon_128")
    private final String icon128;

    @JsonProperty("icon_512")
    private final String icon512;

    @JsonProperty("icon_svg")
    private final String iconSvg;

    /**
     * Constructs a {@code Icon}.
     *
     * @param icon128 The URL of the 128 by 128 pixels raster icon.
     * @param icon512 The URL of the 512 by 512 pixels raster icon.
     * @param iconSvg The URL of the vector icon.
     */
    @JsonCreator
    public Icon(@JsonProperty("icon_128") final String icon128,
                @JsonProperty("icon_512") final String icon512,
                @JsonProperty("icon_svg") final String iconSvg) {
        this.icon128 = icon128;
        this.icon512 = icon512;
        this.iconSvg = iconSvg;
    }

    /**
     * Returns the URL of the 128 by 128 pixels raster icon.
     *
     * @return The URL of the 128 by 128 pixels raster icon.
     */
    public String getIcon128() {
        return icon128;
    }

    /**
     * Returns the URL of the 512 by 512 pixels raster icon.
     *
     * @return The URL of the 512 by 512 pixels raster icon.
     */
    public String getIcon512() {
        return icon512;
    }

    /**
     * Returns the URL of the vector icon.
     *
     * @return The URL of the vector icon.
     */
    public String getIconSvg() {
        return iconSvg;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Icon)) {
            return false;
        }

        Icon that = (Icon) other;

        return Objects.equals(icon128, that.icon128)
                && Objects.equals(icon512, that.icon512)
                && Objects.equals(iconSvg, that.iconSvg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon128, icon512, iconSvg);
    }

    @Override
    public String toString() {
        return "Icon{"
                + "icon128=" + icon128
                + ", icon512=" + icon512
                + ", iconSvg=" + iconSvg
                + '}';
    }
}
