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
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringCacheStorePutIfAbsentTest implements SpringCacheStoreTestHelper {

    @Mock
    private Cache mockCache;

    @Captor
    private ArgumentCaptor<CacheData<String>> cacheDataCaptor;

    private CacheStore<String, String> cacheStore;

    @BeforeEach
    void beforeEach() {
        cacheStore = new CaffeineCacheStore<>(mockCache, DURATION_TEN_MINUTES, TEST_FIXED_CLOCK);
    }

    @Test
    void givenAbsentCacheKey_shouldStoreEntry_whenPutIfAbsent() {
        when(mockCache.get(CACHE_KEY)).thenReturn(null);

        cacheStore.putIfAbsent(CACHE_KEY, CACHE_VALUE);

        verify(mockCache).put(eq(CACHE_KEY), cacheDataCaptor.capture());

        assertThat(cacheDataCaptor.getValue())
            .satisfies(data ->
                    assertCacheData(data, CACHE_VALUE, DURATION_TEN_MINUTES, Instant.now(TEST_FIXED_CLOCK)));
    }

    @Test
    void givenExistentCacheKey_shouldNotStoreEntry_whenPutIfAbsent() {
        var wrapper = mock(Cache.ValueWrapper.class);

        when(mockCache.get(CACHE_KEY)).thenReturn(wrapper);
        when(wrapper.get()).thenReturn(EXISTING_CACHE_VALUE);

        cacheStore.putIfAbsent(CACHE_KEY, CACHE_VALUE);

        verify(mockCache, never()).put(any(), any());
    }

    @Test
    void givenNullCacheKey_shouldThrowNullPointerException_whenPutIfAbsent() {
        assertThatThrownBy(() -> cacheStore.putIfAbsent(null, CACHE_VALUE))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(CacheErrorMessages.KEY_MUST_NOT_BE_NULL);

        verifyNoInteractions(mockCache);
    }

    @Test
    void givenNullCacheValue_shouldThrowNullPointerException_whenPutIfAbsent() {
        assertThatThrownBy(() -> cacheStore.putIfAbsent(CACHE_KEY, (String) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(CacheErrorMessages.VALUE_MUST_NOT_BE_NULL);

        verifyNoInteractions(mockCache);
    }

    @Test
    void givenAbsentCacheKey_shouldStoreEntry_whenPutIfAbsentWithSupplier() {
        when(mockCache.get(CACHE_KEY)).thenReturn(null);

        cacheStore.putIfAbsent(CACHE_KEY, () -> CACHE_VALUE);

        verify(mockCache).put(eq(CACHE_KEY), cacheDataCaptor.capture());

        assertThat(cacheDataCaptor.getValue())
                .satisfies(data ->
                        assertCacheData(data, CACHE_VALUE, DURATION_TEN_MINUTES, Instant.now(TEST_FIXED_CLOCK)));
    }

    @Test
    void givenExistentCacheKey_shouldNotStoreEntry_whenPutIfAbsentWithSupplier() {
        var wrapper = mock(Cache.ValueWrapper.class);

        when(mockCache.get(CACHE_KEY)).thenReturn(wrapper);
        when(wrapper.get()).thenReturn(EXISTING_CACHE_VALUE);

        @SuppressWarnings("unchecked")
        Supplier<String> supplierMock = mock(Supplier.class);

        cacheStore.putIfAbsent(CACHE_KEY, supplierMock);

        verify(mockCache, never()).put(any(), any());
        verifyNoInteractions(supplierMock);
    }

    @Test
    void givenNullCacheKey_shouldThrowNullPointerException_whenPutIfAbsentWithSupplier() {
        assertThatThrownBy(() -> cacheStore.putIfAbsent(null, () -> CACHE_VALUE))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(CacheErrorMessages.KEY_MUST_NOT_BE_NULL);

        verifyNoInteractions(mockCache);
    }

    @Test
    void givenNullSupplier_shouldThrowNullPointerException_whenPutIfAbsentWithSupplier() {
        assertThatThrownBy(() -> cacheStore.putIfAbsent(CACHE_KEY, (Supplier<String>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(CacheErrorMessages.VALUE_SUPPLIER_MUST_NOT_BE_NULL);

        verifyNoInteractions(mockCache);
    }

    @Test
    void givenSupplierThatReturnsNull_shouldThrowNullPointerException_whenPutIfAbsentWithSupplier() {
        when(mockCache.get(CACHE_KEY)).thenReturn(null);

        assertThatThrownBy(() -> cacheStore.putIfAbsent(CACHE_KEY, () -> null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(CacheErrorMessages.VALUE_MUST_NOT_BE_NULL);

        verify(mockCache, never()).put(any(), any());
    }
}
