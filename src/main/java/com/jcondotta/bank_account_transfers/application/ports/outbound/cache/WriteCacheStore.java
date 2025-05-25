package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

public interface WriteCacheStore<K, V>
        extends CacheStorePut<K, V>, CacheStorePutIfAbsent<K, V>, CacheStorePutIfAbsentOrStale<K, V> {

    void evict(K cacheKey);
}