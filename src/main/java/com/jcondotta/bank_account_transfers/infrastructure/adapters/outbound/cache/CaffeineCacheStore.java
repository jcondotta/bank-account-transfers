package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class CaffeineCacheStore<K, V> implements CacheStore<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaffeineCacheStore.class);

    private final Cache<K, V> cache;

    public CaffeineCacheStore(Cache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public void set(K cacheKey, V cacheValue) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");
        Objects.requireNonNull(cacheValue, "cache.value.notNull");

        LOGGER.debug("Caffeine cache adding entry:  Key='{}', Value={}", cacheKey, cacheValue);

        cache.put(cacheKey, cacheValue);

        LOGGER.atInfo().setMessage("Cache store: Key='{}' successfully stored in cache.")
                .addArgument(cacheKey)
                .addKeyValue("cacheKey", cacheKey).log();
    }

    @Override
    public Optional<V> get(K cacheKey) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");
        V cachedValue = cache.getIfPresent(cacheKey);

        if (Objects.nonNull(cachedValue)) {
            LOGGER.info("Cache hit: Key='{}' -> Value={}", cacheKey, cachedValue);
        }
        else {
            LOGGER.atInfo().setMessage("Cache miss: Key='{}' not found.")
                    .addArgument(cacheKey)
                    .addKeyValue("cacheKey", cacheKey).log();
        }

        return Optional.ofNullable(cachedValue);
    }

    @Override
    public Optional<V> getOrFetch(K cacheKey, Function<K, Optional<V>> valueLoader) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");
        Objects.requireNonNull(valueLoader, "cache.valueLoader.function.notNull");

        return Optional.ofNullable(cache.get(cacheKey, k -> {
            LOGGER.warn("Cache miss: Key='{}' not found, fetching from external source.", k);
            Optional<V> value = valueLoader.apply(k);

            if (value.isEmpty()) {
                LOGGER.warn("valueLoader returned empty for Key='{}'", k);
            }
            else {
                LOGGER.info("Value loaded and cached: Key='{}'", k);
            }
            return value.orElse(null);

        })).map(value -> {
            LOGGER.debug("Cache hit: Key='{}' -> Value={}", cacheKey, value);
            return value;
        });
    }
}