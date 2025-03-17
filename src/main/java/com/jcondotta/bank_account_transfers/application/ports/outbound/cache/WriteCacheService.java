package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

public interface WriteCacheService<K, V> {

    void set(K cacheKey, V cacheValue);
}
