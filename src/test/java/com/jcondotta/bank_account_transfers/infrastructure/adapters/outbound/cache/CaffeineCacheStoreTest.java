package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaffeineCacheStoreTest {

    private static final String CACHE_KEY = "cache-key:6d988294-b01c-4651-8af7-fb90d31aa35a";
    private static final String CACHE_VALUE = "{\"name\": \"java\"}";

    @Mock
    private Cache mockCache;

    @Mock
    private Function<String, Optional<String>> valueLoaderMock;

    private CacheStore<String, String> cacheStore;

    @BeforeEach
    public void beforeEach(){
        cacheStore = new CaffeineCacheStore<>(mockCache);
    }

    @Test
    void shouldStoreEntryInCache_whenCacheEntryIsValid() {
        cacheStore.set(CACHE_KEY, CACHE_VALUE);

        verify(mockCache).put(eq(CACHE_KEY), eq(CACHE_VALUE));
    }

    @Test
    void shouldThrowNullPointerException_whenSetEntryCacheKeyIsNull() {
        assertThatThrownBy(() -> cacheStore.set(null, CACHE_VALUE))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("cache.key.notNull");

        verify(mockCache, never()).put(anyString(), anyString());
    }

    @Test
    void shouldThrowNullPointerException_whenSetEntryCacheValueIsNull() {
        assertThatThrownBy(() -> cacheStore.set(CACHE_KEY, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("cache.value.notNull");

        verify(mockCache, never()).put(anyString(), anyString());
    }

    @Test
    void shouldReturnCachedValue_whenCacheKeyExists() {
        when(mockCache.get(CACHE_KEY)).thenReturn(new SimpleValueWrapper(CACHE_VALUE));

        assertThat(cacheStore.getIfPresent(CACHE_KEY))
                .isPresent()
                .hasValue(CACHE_VALUE);

        verify(mockCache).get(CACHE_KEY);
    }

    @Test
    void shouldReturnEmptyOption_whenMissingCacheKey() {
        when(mockCache.get(CACHE_KEY)).thenReturn(null);

        assertThat(cacheStore.getIfPresent(CACHE_KEY))
                .isEmpty();
    }

    @Test
    void shouldThrowNullPointerException_whenValueLoaderIsNullInGetOrFetch() {
        assertThatThrownBy(() -> cacheStore.getOrFetch(CACHE_KEY, (Function<String, Optional<String>>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("cache.valueLoader.function.notNull");

        verify(mockCache, never()).put(anyString(), anyString());
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnCachedValue_whenKeyExistsInGetOrFetch() {
        when(mockCache.get(eq(CACHE_KEY), any(Callable.class)))
                .thenReturn(CACHE_VALUE);

        Optional<String> cacheValue = cacheStore.getOrFetch(CACHE_KEY, valueLoaderMock);

        assertThat(cacheValue).hasValue(CACHE_VALUE);

        verify(mockCache).get(eq(CACHE_KEY), any(Callable.class));
        verifyNoMoreInteractions(mockCache);
        verifyNoInteractions(valueLoaderMock);
    }

    @Test
    void shouldFetchAndCacheValue_whenCacheMissInGetOrFetch() {
        when(mockCache.get(eq(CACHE_KEY), any(Callable.class)))
                .thenAnswer(invocation -> {
                    Callable<String> loader = invocation.getArgument(1);
                    return loader.call();
                });

        Optional<String> cacheValue = cacheStore.getOrFetch(CACHE_KEY,
                key -> Optional.of(CACHE_VALUE));

        assertThat(cacheValue).hasValue(CACHE_VALUE);

        verify(mockCache).get(eq(CACHE_KEY), any(Callable.class));
    }

    @Test
    void shouldReturnCachedValueAndNotCallValueLoader_whenCachedValueExists() {
        when(mockCache.get(eq(CACHE_KEY), any(Callable.class)))
                .thenReturn(CACHE_VALUE);

        Optional<String> cacheValue = cacheStore.getOrFetch(CACHE_KEY, valueLoaderMock);

        assertThat(cacheValue).hasValue(CACHE_VALUE);

        verify(mockCache).get(eq(CACHE_KEY), any(Callable.class));
        verifyNoInteractions(valueLoaderMock);
    }

    @Test
    void shouldReturnSameCachedValue_whenMultipleThreadsAccessCacheConcurrently() {
        Cache springCache = new CaffeineCache("test-cache", Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build());

        cacheStore = new CaffeineCacheStore<>(springCache);

        when(valueLoaderMock.apply(eq(CACHE_KEY)))
                .thenReturn(Optional.of(CACHE_VALUE));

        int threadCount = 4;

        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            List<Optional<String>> results = IntStream.range(0, threadCount)
                    .mapToObj(i -> CompletableFuture.supplyAsync(
                            () -> cacheStore.getOrFetch(CACHE_KEY, valueLoaderMock), executor))
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            assertThat(results)
                    .as("All threads must retrieve the same cached value: %s", CACHE_VALUE)
                    .isNotEmpty()
                    .allSatisfy(value -> assertThat(value)
                            .as("Each value must be present and equal to the cached value")
                            .hasValue(CACHE_VALUE));

            verify(valueLoaderMock, times(1)).apply(eq(CACHE_KEY));
        }
    }

    @Test
    void shouldThrowNullPointerException_whenInvalidatingNullKey() {
        assertThatThrownBy(() -> cacheStore.evict(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("cache.key.notNull");

        verify(mockCache, never()).evict(anyString());
    }

    @Test
    void shouldEvictCacheEntry_whenEvictingExistingKey() {
        doNothing().when(mockCache).evict(CACHE_KEY);

        cacheStore.evict(CACHE_KEY);

        verify(mockCache).evict(eq(CACHE_KEY));
    }

    @Test
    void shouldDoNothing_whenEvictingNonExistingKey() {
        doNothing().when(mockCache).evict(CACHE_KEY);

        cacheStore.evict(CACHE_KEY);

        verify(mockCache).evict(eq(CACHE_KEY));
    }
}