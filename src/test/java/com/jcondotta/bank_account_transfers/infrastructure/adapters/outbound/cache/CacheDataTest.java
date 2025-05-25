package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class CacheDataTest {

    private static final String CACHE_DATA_VALUE = "{\"name\": \"java 21\"}";

    private static final Clock TEST_FIXED_CLOCK = Clock.fixed(Instant.parse("2022-06-24T12:45:01Z"), ZoneOffset.UTC);
    private static final Duration DURATION_TWENTY_SECONDS = Duration.ofSeconds(20);

    @Test
    void of_shouldThrowNullPointerException_whenDataIsNull() {
        assertThatThrownBy(() -> CacheData.of(null, DURATION_TWENTY_SECONDS, TEST_FIXED_CLOCK))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(CacheDataErrorMessages.DATA_MUST_NOT_BE_NULL);
    }

    @Test
    void of_shouldThrowNullPointerException_whenTimeToLiveIsNull() {
        assertThatThrownBy(() -> CacheData.of(CACHE_DATA_VALUE, null, TEST_FIXED_CLOCK))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(CacheDataErrorMessages.TTL_MUST_NOT_BE_NULL);
    }

    @Test
    void of_shouldThrowNullPointerException_whenClockIsNull() {
        assertThatThrownBy(() -> CacheData.of(CACHE_DATA_VALUE, DURATION_TWENTY_SECONDS, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(CacheDataErrorMessages.CLOCK_MUST_NOT_BE_NULL);
    }

    @Test
    void of_shouldUseProvidedClock_whenCreatingCacheData() {
        var cacheData = CacheData.of(CACHE_DATA_VALUE, DURATION_TWENTY_SECONDS, TEST_FIXED_CLOCK);

        assertAll(
                () -> assertThat(cacheData.data()).isEqualTo(CACHE_DATA_VALUE),
                () -> assertThat(cacheData.timeToLive()).isEqualTo(DURATION_TWENTY_SECONDS),
                () -> assertThat(cacheData.cachedAt()).isEqualTo(Instant.now(TEST_FIXED_CLOCK))
        );
    }

    @Test
    void of_shouldUseSystemClock_whenCreatingCacheDataWithoutClock() {
        var before = Instant.now(Clock.systemUTC());
        var cacheData = CacheData.of(CACHE_DATA_VALUE, DURATION_TWENTY_SECONDS);
        var after = Instant.now(Clock.systemUTC());

        assertThat(cacheData.data()).isEqualTo(CACHE_DATA_VALUE);
        assertThat(cacheData.timeToLive()).isEqualTo(DURATION_TWENTY_SECONDS);
        assertThat(cacheData.cachedAt()).isBetween(before, after);
    }

    @Test
    void hasExpired_shouldReturnFalse_whenCacheIsNotExpiredYet() {
        var cacheData = CacheData.of(CACHE_DATA_VALUE, DURATION_TWENTY_SECONDS, TEST_FIXED_CLOCK);

        Clock fiveSecondsAfterCurrentClock = Clock.fixed(
                TEST_FIXED_CLOCK.instant().plus(Duration.ofSeconds(5)),
                ZoneOffset.UTC
        );

        assertThat(cacheData.hasExpired(fiveSecondsAfterCurrentClock)).isFalse();
    }

    @Test
    void hasExpired_shouldReturnTrue_whenCacheIsExpired() {
        var cacheData = CacheData.of(CACHE_DATA_VALUE, DURATION_TWENTY_SECONDS, TEST_FIXED_CLOCK);

        Clock oneMinuteAfterCurrentClock = Clock.fixed(
                TEST_FIXED_CLOCK.instant().plus(Duration.ofMinutes(1)),
                ZoneOffset.UTC
        );

        assertThat(cacheData.hasExpired(oneMinuteAfterCurrentClock)).isTrue();
    }

    @Test
    void hasExpired_shouldReturnTrue_whenCurrentTimeEqualsExpiryTime() {
        var cacheData = CacheData.of(CACHE_DATA_VALUE, DURATION_TWENTY_SECONDS, TEST_FIXED_CLOCK);

        Clock atExpiry = Clock.fixed(
                TEST_FIXED_CLOCK.instant().plus(DURATION_TWENTY_SECONDS),
                ZoneOffset.UTC
        );

        assertThat(cacheData.hasExpired(atExpiry)).isTrue();
    }

    @Test
    void hasExpired_shouldReturnTrue_whenTimeToLiveIsZero() {
        var instantExpireCacheData = CacheData.of(CACHE_DATA_VALUE, Duration.ZERO, TEST_FIXED_CLOCK);

        assertThat(instantExpireCacheData.hasExpired(TEST_FIXED_CLOCK)).isTrue();
    }

    @Test
    void hasExpired_shouldReturnFalse_whenTimeToLiveIsVeryLarge() {
        var durationOfTenYears = Duration.ofDays(365 * 10);
        var cacheData = CacheData.of(CACHE_DATA_VALUE, durationOfTenYears, TEST_FIXED_CLOCK);

        Clock oneDayAfterCurrentClock = Clock.fixed(
                TEST_FIXED_CLOCK.instant().plus(Duration.ofDays(1)),
                ZoneOffset.UTC
        );

        assertThat(cacheData.hasExpired(oneDayAfterCurrentClock)).isFalse();
    }
}
