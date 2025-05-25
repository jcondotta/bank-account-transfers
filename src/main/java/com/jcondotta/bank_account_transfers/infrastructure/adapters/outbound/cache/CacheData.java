package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class CacheData<T> {

    private final T data;
    private final Instant cachedAt;
    private final Duration timeToLive;

    private CacheData(T data, Instant cachedAt, Duration timeToLive) {
        this.data = Objects.requireNonNull(data, CacheDataErrorMessages.DATA_MUST_NOT_BE_NULL);
        this.cachedAt = Objects.requireNonNull(cachedAt, CacheDataErrorMessages.CACHED_AT_MUST_NOT_BE_NULL);
        this.timeToLive = Objects.requireNonNull(timeToLive, CacheDataErrorMessages.TTL_MUST_NOT_BE_NULL);
    }

    public static <T> CacheData<T> of(T data, Duration timeToLive, Clock clock) {
        Objects.requireNonNull(clock, CacheDataErrorMessages.CLOCK_MUST_NOT_BE_NULL);
        return new CacheData<>(data, Instant.now(clock), timeToLive);
    }

    public static <T> CacheData<T> of(T data, Duration timeToLive) {
        return new CacheData<>(data, Instant.now(Clock.systemUTC()), timeToLive);
    }

    public boolean hasExpired(Clock clock) {
        return !Instant.now(clock).isBefore(cachedAt.plus(timeToLive));
    }

    public T data() {
        return data;
    }

    public Instant cachedAt() {
        return cachedAt;
    }

    public Duration timeToLive() {
        return timeToLive;
    }
}
