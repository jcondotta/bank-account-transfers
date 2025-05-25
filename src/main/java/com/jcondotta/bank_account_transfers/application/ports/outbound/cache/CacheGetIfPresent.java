package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

import java.util.Optional;

public interface CacheGetIfPresent<K, V> {
    Optional<V> getIfPresent(K cacheKey);
}