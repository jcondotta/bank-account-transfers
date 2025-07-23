package com.jcondotta.bank_account_transfers.domain.shared.valueobjects;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.util.Objects.requireNonNull;

public record OccurredAt(Instant value, ZoneId zoneId) {

    public static final String INSTANT_VALUE_NOT_NULL_MESSAGE = "instant value must not be null.";
    public static final String ZONE_ID_NOT_NULL_MESSAGE = "zone id must not be null.";
    public static final String CLOCK_NOT_NULL_MESSAGE = "clock must not be null.";
    public static final String ZONED_DATE_TIME_NOT_NULL_MESSAGE = "zoned date time must not be null.";

    public OccurredAt {
        requireNonNull(value, INSTANT_VALUE_NOT_NULL_MESSAGE);
        requireNonNull(zoneId, ZONE_ID_NOT_NULL_MESSAGE);
    }

    public static OccurredAt now(Clock clock) {
        requireNonNull(clock, CLOCK_NOT_NULL_MESSAGE);
        return new OccurredAt(clock.instant(), clock.getZone());
    }

    public static OccurredAt of(Instant instant, ZoneId zoneId) {
        return new OccurredAt(instant, zoneId);
    }

    public static OccurredAt of(ZonedDateTime zonedDateTime) {
        requireNonNull(zonedDateTime, ZONED_DATE_TIME_NOT_NULL_MESSAGE);
        return new OccurredAt(zonedDateTime.toInstant(), zonedDateTime.getZone());
    }

    public ZonedDateTime asZonedDateTime() {
        return value.atZone(zoneId);
    }

    @Override
    public String toString() {
        return asZonedDateTime().toString();
    }
}
