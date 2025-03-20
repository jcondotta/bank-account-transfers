package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

import java.util.Optional;
import java.util.function.Function;

public interface ReadCacheStore<K, V> {

    Optional<V> get(K cacheKey);
    Optional<V> getOrFetch(K cacheKey, Function<K, Optional<V>> valueLoader);
}