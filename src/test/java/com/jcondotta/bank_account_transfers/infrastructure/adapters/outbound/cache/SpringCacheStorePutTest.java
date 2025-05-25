package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SpringCacheStorePutTest implements SpringCacheStoreTestHelper{

    @Mock
    private Cache mockCache;

    @Captor
    private ArgumentCaptor<CacheData<String>> cacheDataCaptor;

    private CacheStore<String, String> cacheStore;

    @BeforeEach
    public void beforeEach(){
        cacheStore = new CaffeineCacheStore<>(mockCache, DURATION_TEN_MINUTES, TEST_FIXED_CLOCK);
    }

    @Test
    void givenAbsentCacheKey_shouldStoreEntry_whenPut() {
        cacheStore.put(CACHE_KEY, CACHE_VALUE);

        verify(mockCache).put(eq(CACHE_KEY), cacheDataCaptor.capture());

        assertThat(cacheDataCaptor.getValue())
                .satisfies(cacheData ->
                        assertCacheData(cacheData, CACHE_VALUE, DURATION_TEN_MINUTES, Instant.now(TEST_FIXED_CLOCK))
                );
    }

    @Test
    void givenNullCacheKey_shouldThrowNullPointerException_whenPut() {
        assertThatThrownBy(() -> cacheStore.put(null, CACHE_VALUE))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(CacheErrorMessages.KEY_MUST_NOT_BE_NULL);

        verifyNoInteractions(mockCache);
    }

    @Test
    void givenNullCacheValue_shouldThrowNullPointerException_whenPut() {
        assertThatThrownBy(() -> cacheStore.put(CACHE_KEY, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(CacheErrorMessages.VALUE_MUST_NOT_BE_NULL);

        verifyNoInteractions(mockCache);
    }
}