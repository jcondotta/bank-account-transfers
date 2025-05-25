package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

import java.time.Duration;
import java.util.function.Supplier;

public interface CacheStorePutIfAbsentOrStale<K, V> {

    /**
     * Stores the value if there is no value or if the existing value is stale (expired).
     */
    void putIfAbsentOrStale(K cacheKey, V cacheValue);

    /**
     * Stores the value with TTL if there is no value or the existing value is stale (expired).
     */
    void putIfAbsentOrStale(K cacheKey, V cacheValue, Duration timeToLive);

    /**
     * Stores the value provided by a supplier if there is no value or if the existing value is stale (expired).
     */
    void putIfAbsentOrStale(K cacheKey, Supplier<V> valueSupplier);

    /**
     * Stores the value provided by a supplier with TTL if there is no value or if the existing value is stale (expired).
     */
    void putIfAbsentOrStale(K cacheKey, Supplier<V> valueSupplier, Duration timeToLive);
}

