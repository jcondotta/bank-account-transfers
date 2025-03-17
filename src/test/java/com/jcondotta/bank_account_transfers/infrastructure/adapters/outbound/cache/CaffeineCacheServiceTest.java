package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class CaffeineCacheServiceTest {

    private static final String CACHE_KEY = "cache-key:6d988294-b01c-4651-8af7-fb90d31aa35a";
    private static final String CACHE_VALUE = "{\"name\": \"java\"}";

    @Mock
    private Cache<String, String> mockCache;

    @Mock
    private Function<String, Optional<String>> valueLoaderMock;

    private CacheService<String, String> cacheService;

    @BeforeEach
    public void beforeEach(){
        cacheService = new CaffeineCacheService<>(mockCache);
    }

    @Test
    void shouldStoreEntryInCache_whenCacheEntryIsValid() {
        cacheService.set(CACHE_KEY, CACHE_VALUE);

        verify(mockCache).put(eq(CACHE_KEY), eq(CACHE_VALUE));
    }

    @Test
    void shouldThrowNullPointerException_whenSetEntryCacheKeyIsNull() {
        assertThatThrownBy(() -> cacheService.set(null, CACHE_VALUE))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("cache.key.notNull");

        verify(mockCache, never()).put(anyString(), anyString());
    }

    @Test
    void shouldThrowNullPointerException_whenSetEntryCacheValueIsNull() {
        assertThatThrownBy(() -> cacheService.set(CACHE_KEY, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("cache.value.notNull");

        verify(mockCache, never()).put(anyString(), anyString());
    }

    @Test
    void shouldReturnCachedValue_whenCacheKeyExists() {
        when(mockCache.getIfPresent(CACHE_KEY))
                .thenReturn(CACHE_VALUE);

        Optional<String> optCacheValue = cacheService.get(CACHE_KEY);
        assertThat(optCacheValue)
                .isPresent()
                .hasValue(CACHE_VALUE);

        verify(mockCache).getIfPresent(CACHE_KEY);
    }

    @Test
    void shouldReturnEmptyOption_whenMissingCacheKey() {
        when(mockCache.getIfPresent(CACHE_KEY))
                .thenReturn(null);

        Optional<String> optCacheValue = cacheService.get(CACHE_KEY);
        assertThat(optCacheValue).isEmpty();

        verify(mockCache).getIfPresent(CACHE_KEY);
    }

    @Test
    void shouldThrowNullPointerException_whenValueLoaderIsNullInGetOrFetch() {
        assertThatThrownBy(() -> cacheService.getOrFetch(CACHE_KEY, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("cache.valueLoader.function.notNull");

        verify(mockCache, never()).put(anyString(), anyString());
    }

    @Test
    void shouldReturnCachedValue_whenKeyExistsInGetOrFetch(){
        when(mockCache.get(eq(CACHE_KEY), any())).thenReturn(CACHE_VALUE);

        Optional<String> cacheValue = cacheService.getOrFetch(CACHE_KEY, valueLoaderMock);
        assertThat(cacheValue).hasValue(CACHE_VALUE);

        verify(mockCache).get(eq(CACHE_KEY), any());
        verifyNoMoreInteractions(mockCache);
    }

    @Test
    void shouldFetchAndCacheValue_whenCacheMissInGetOrFetch() {
        when(mockCache.get(eq(CACHE_KEY), any()))
                .thenAnswer(invocation -> {
                    Function<String, String> loader = invocation.getArgument(1);
                    return loader.apply(CACHE_KEY);
                });

        Optional<String> cacheValue = cacheService.getOrFetch(CACHE_KEY,
                key -> Optional.of(CACHE_VALUE));
        assertThat(cacheValue).hasValue(CACHE_VALUE);

        verify(mockCache).get(eq(CACHE_KEY), any());
    }

    @Test
    void shouldReturnCachedValueAndNotCallValueLoader_whenCachedValueExists() {
        when(mockCache.get(eq(CACHE_KEY), any())).thenReturn(CACHE_VALUE);

        Optional<String> cacheValue = cacheService.getOrFetch(CACHE_KEY, valueLoaderMock);
        assertThat(cacheValue).hasValue(CACHE_VALUE);

        verify(mockCache).get(eq(CACHE_KEY), any());
        verify(valueLoaderMock, never()).apply(anyString());
    }

    @Test
    void shouldReturnSameCachedValue_whenMultipleThreadsAccessCacheConcurrently() throws ExecutionException, InterruptedException {
        cacheService = new CaffeineCacheService<>(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build());

        when(valueLoaderMock.apply(eq(CACHE_KEY)))
                .thenAnswer(invocation -> Optional.of(CACHE_VALUE));

        int threadCount = 4;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        try{
            List<Optional<String>> results = IntStream.range(0, threadCount)
                    .mapToObj(i -> CompletableFuture.supplyAsync(() -> cacheService.getOrFetch(CACHE_KEY, valueLoaderMock), executor))
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            assertThat(results)
                    .as("Expected all threads to retrieve the same cached value: %s", CACHE_VALUE)
                    .isNotEmpty()
                    .allSatisfy(value -> assertThat(value)
                            .as("Each value should be present and equal to the cached value")
                            .hasValue(CACHE_VALUE));

            verify(valueLoaderMock, times(1)).apply(eq(CACHE_KEY));
        }
        finally {
            executor.shutdown();
        }
    }
}