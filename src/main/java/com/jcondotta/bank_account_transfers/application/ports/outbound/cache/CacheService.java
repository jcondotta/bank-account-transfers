package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

public interface CacheService<K, V> extends ReadCacheService<K, V>, WriteCacheService<K, V> {
}