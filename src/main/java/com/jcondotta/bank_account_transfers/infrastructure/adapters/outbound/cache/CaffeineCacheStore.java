package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class CaffeineCacheStore<K, V> implements CacheStore<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaffeineCacheStore.class);

    private final Cache cache;

    public CaffeineCacheStore(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void set(K cacheKey, V cacheValue) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");
        Objects.requireNonNull(cacheValue, "cache.value.notNull");

        LOGGER.debug("Caffeine (Spring) cache adding entry: Key='{}', Value={}", cacheKey, cacheValue);
        cache.put(cacheKey, cacheValue);

        LOGGER.atInfo().setMessage("Cache store: Key='{}' successfully stored in cache.")
                .addArgument(cacheKey)
                .addKeyValue("cacheKey", cacheKey).log();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<V> getIfPresent(K cacheKey) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");
        var wrapper = cache.get(cacheKey);

        if (Objects.nonNull(wrapper)) {
            V value = (V) wrapper.get();
            LOGGER.info("Cache hit: Key='{}' -> Value={}", cacheKey, value);

            return Optional.ofNullable(value);
        }
        else {
            LOGGER.info("Cache miss: Key='{}' not found.", cacheKey);

            return Optional.empty();
        }
    }

    public Optional<V> getOrFetch(K cacheKey, Supplier<Optional<V>> cacheValueLoader){
        return getOrFetch(cacheKey, ignored -> cacheValueLoader.get());
    }

    @Override
    public Optional<V> getOrFetch(K cacheKey, Function<K, Optional<V>> cacheValueLoader) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");
        Objects.requireNonNull(cacheValueLoader, "cache.valueLoader.function.notNull");

        try {
            V value = cache.get(cacheKey, () -> fetchAndCacheValue(cacheKey, cacheValueLoader));
            if (value == null) {
                return Optional.empty();
            }
            else {
                LOGGER.debug("Cache hit (via getOrFetch): Key='{}' -> Value={}", cacheKey, value);
                return Optional.of(value);
            }
        }
        catch (Cache.ValueRetrievalException e) {
            LOGGER.error("Error retrieving cache value for key '{}'", cacheKey, e);
            throw e;
        }
    }

    private V fetchAndCacheValue(K cacheKey, Function<K, Optional<V>> cacheValueLoader) {
        LOGGER.warn("Cache miss: Key='{}' not found, fetching from external source.", cacheKey);

        Optional<V> cacheValueLoaded = cacheValueLoader.apply(cacheKey);
        if (cacheValueLoaded.isEmpty()) {
            LOGGER.warn("valueLoader returned empty for Key='{}'", cacheKey);
        }
        else {
            LOGGER.info("Value loaded and cached: Key='{}'", cacheKey);
        }
        return cacheValueLoaded.orElse(null);
    }

    @Override
    public void evict(K cacheKey) {
        Objects.requireNonNull(cacheKey, "cache.key.notNull");

        LOGGER.info("Evicting entry for Key='{}'", cacheKey);
        cache.evict(cacheKey);

        LOGGER.debug("Cache evict: Key='{}' removed from cache.", cacheKey);
    }
}
