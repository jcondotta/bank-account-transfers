package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@Embeddable
public class CreatedAtEmbeddable {

    protected CreatedAtEmbeddable() {}

    public CreatedAtEmbeddable(Instant createdAt, String createdAtZoneId) {
        this.createdAt = createdAt;
        this.createdAtZoneId = createdAtZoneId;
    }

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "created_at_zone_id", nullable = false, length = 50)
    private String createdAtZoneId;

    public static CreatedAtEmbeddable of(Instant createdAt, ZoneId createdAtZoneId) {
        return new CreatedAtEmbeddable(createdAt, createdAtZoneId.getId());
    }

    public static CreatedAtEmbeddable of(ZonedDateTime zonedDateTime) {
        return new CreatedAtEmbeddable(zonedDateTime.toInstant(), zonedDateTime.getZone().getId());
    }
}
