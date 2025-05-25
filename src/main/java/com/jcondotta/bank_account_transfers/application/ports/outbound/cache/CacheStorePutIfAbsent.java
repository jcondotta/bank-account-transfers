package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

import java.time.Duration;
import java.util.function.Supplier;

public interface CacheStorePutIfAbsent<K, V> {

    /**
     * Stores the value only if there is no existing value.
     */
    void putIfAbsent(K cacheKey, V cacheValue);

    /**
     * Stores the value only if there is no existing value.
     * The value will be provided lazily using a Supplier.
     */
    void putIfAbsent(K cacheKey, Supplier<V> valueSupplier);

    /**
     * Stores the value with a TTL only if there is no existing value.
     */
    void putIfAbsent(K cacheKey, V cacheValue, Duration timeToLive);

    /**
     * Stores the value with a TTL only if there is no existing value.
     * The value will be provided lazily using a Supplier.
     */
    void putIfAbsent(K cacheKey, Supplier<V> valueSupplier, Duration timeToLive);
}
