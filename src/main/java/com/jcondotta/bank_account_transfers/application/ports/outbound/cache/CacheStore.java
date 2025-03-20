package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

public interface CacheStore<K, V> extends ReadCacheStore<K, V>, WriteCacheStore<K, V> {
}