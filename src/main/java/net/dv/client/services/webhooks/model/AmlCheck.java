package net.dv.client.services.webhooks.model;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The anti money laundering screening that blocked a payment.
 */
public final class AmlCheck {

    private final String id;
    private final String score;
    private final String status;
    private final String riskLevel;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    /**
     * Constructs an {@code AmlCheck}.
     *
     * @param id        The screening identifier.
     * @param score     The risk score reported by the provider, as a decimal string.
     * @param status    The screening status, for example {@code success}.
     * @param riskLevel The risk level: {@code none}, {@code low}, {@code medium}, {@code high},
     *                  {@code critical} or {@code undefined}; {@code null} when the provider gave none.
     * @param createdAt The moment the screening started.
     * @param updatedAt The moment the screening was last updated.
     */
    public AmlCheck(final String id,
                    final String score,
                    final String status,
                    final String riskLevel,
                    final OffsetDateTime createdAt,
                    final OffsetDateTime updatedAt) {
        this.id = id;
        this.score = score;
        this.status = status;
        this.riskLevel = riskLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the screening identifier.
     *
     * @return The screening identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the risk score reported by the provider, as a decimal string.
     *
     * @return The risk score.
     */
    public String getScore() {
        return score;
    }

    /**
     * Returns the screening status, for example {@code success}.
     *
     * @return The screening status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the risk level: {@code none}, {@code low}, {@code medium}, {@code high},
     * {@code critical} or {@code undefined}.
     *
     * @return The risk level, {@code null} when the provider gave none.
     */
    public String getRiskLevel() {
        return riskLevel;
    }

    /**
     * Returns the moment the screening started.
     *
     * @return The moment the screening started.
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the moment the screening was last updated.
     *
     * @return The moment the screening was last updated.
     */
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AmlCheck)) {
            return false;
        }

        AmlCheck that = (AmlCheck) other;

        return Objects.equals(id, that.id)
                && Objects.equals(score, that.score)
                && Objects.equals(status, that.status)
                && Objects.equals(riskLevel, that.riskLevel)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score, status, riskLevel, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "AmlCheck{"
                + "id=" + id
                + ", score=" + score
                + ", status=" + status
                + ", riskLevel=" + riskLevel
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
