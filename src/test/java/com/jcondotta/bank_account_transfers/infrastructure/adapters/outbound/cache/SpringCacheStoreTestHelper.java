package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public interface SpringCacheStoreTestHelper {

    String CACHE_KEY = "cache-key:123";
    String CACHE_VALUE = "{\"name\": \"java 21\"}";
    String EXISTING_CACHE_VALUE = "{\"name\": \"java 11\"}";

    Duration DURATION_TEN_MINUTES = Duration.ofMinutes(10);
    Duration DURATION_TWENTY_SECONDS = Duration.ofSeconds(20);
    Clock TEST_FIXED_CLOCK = Clock.fixed(Instant.parse("2025-04-12T12:00:00Z"), java.time.ZoneOffset.UTC);

    default void assertCacheData(CacheData<String> actualCacheData, String expectedValue, Duration expectedTTL, Instant expectedTimestamp) {
        assertAll(
            () -> assertThat(actualCacheData.data()).isEqualTo(expectedValue),
            () -> assertThat(actualCacheData.timeToLive()).isEqualTo(expectedTTL),
            () -> assertThat(actualCacheData.cachedAt()).isEqualTo(expectedTimestamp)
        );
    }
}
