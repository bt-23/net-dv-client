package net.dv.client.services.withdrawals.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * The progress of the blockchain transfer backing a withdrawal.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Transfer {

    @JsonProperty("kind")
    private final String kind;

    @JsonProperty("stage")
    private final String stage;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("message")
    private final String message;

    /**
     * Constructs a {@code Transfer}.
     *
     * @param kind    The kind of transfer the processing engine selected.
     * @param stage   The stage the transfer has reached.
     * @param status  The status of the transfer, for example {@code completed} or {@code failed}.
     * @param message The failure detail, {@code null} while the transfer is healthy.
     */
    @JsonCreator
    public Transfer(@JsonProperty("kind") final String kind,
                    @JsonProperty("stage") final String stage,
                    @JsonProperty("status") final String status,
                    @JsonProperty("message") final String message) {
        this.kind = kind;
        this.stage = stage;
        this.status = status;
        this.message = message;
    }

    /**
     * Returns the kind of transfer the processing engine selected.
     *
     * @return The kind of transfer the processing engine selected.
     */
    public String getKind() {
        return kind;
    }

    /**
     * Returns the stage the transfer has reached.
     *
     * @return The stage the transfer has reached.
     */
    public String getStage() {
        return stage;
    }

    /**
     * Returns the status of the transfer, for example {@code completed} or {@code failed}.
     *
     * @return The status of the transfer, for example {@code completed} or {@code failed}.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the failure detail, {@code null} while the transfer is healthy.
     *
     * @return The failure detail, {@code null} while the transfer is healthy.
     */
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Transfer)) {
            return false;
        }

        Transfer that = (Transfer) other;

        return Objects.equals(kind, that.kind)
                && Objects.equals(stage, that.stage)
                && Objects.equals(status, that.status)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, stage, status, message);
    }

    @Override
    public String toString() {
        return "Transfer{"
                + "kind=" + kind
                + ", stage=" + stage
                + ", status=" + status
                + ", message=" + message
                + '}';
    }
}
