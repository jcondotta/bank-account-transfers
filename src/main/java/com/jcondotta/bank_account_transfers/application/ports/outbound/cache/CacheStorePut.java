package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

import java.time.Duration;

public interface CacheStorePut<K, V> {

    /**
        * Stores the value unconditionally, overriding any existing entry.
     */
    void put(K cacheKey, V cacheValue);

    /**
        * Stores the value with a given TTL, overriding any existing entry.
     */
    void put(K cacheKey, V cacheValue, Duration timeToLive);
}
