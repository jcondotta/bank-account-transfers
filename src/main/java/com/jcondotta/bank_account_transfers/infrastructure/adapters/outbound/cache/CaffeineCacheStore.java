package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheAction;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.time.Clock;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class CaffeineCacheStore<K, V> implements CacheStore<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaffeineCacheStore.class);

    private final Cache cache;
    private final Clock clock;
    private final Duration defaultTimeToLive;

    public CaffeineCacheStore(Cache cache, Duration defaultTimeToLive, Clock clock) {
        this.cache = Objects.requireNonNull(cache, CacheErrorMessages.CACHE_MUST_NOT_BE_NULL);
        this.clock = Objects.requireNonNull(clock, CacheErrorMessages.CACHE_MUST_NOT_BE_NULL);
        this.defaultTimeToLive = Objects.requireNonNull(defaultTimeToLive, CacheErrorMessages.DEFAULT_TTL_MUST_NOT_BE_NULL);
    }

    public CaffeineCacheStore(Cache cache, Duration defaultTimeToLive) {
        this(cache, defaultTimeToLive, Clock.systemUTC());
    }

    @Override
    public void put(K cacheKey, V cacheValue) {
        put(cacheKey, cacheValue, defaultTimeToLive);
    }

    @Override
    public void put(K cacheKey, V cacheValue, Duration timeToLive) {
        validateCacheKey(cacheKey);
        validateCacheValue(cacheValue);
        validateCacheEntryTimeToLive(timeToLive);

        CacheData<V> cacheData = CacheData.of(cacheValue, timeToLive, clock);

        LOGGER.atDebug()
                .setMessage("Cache adding entry: key='{}', data='{}', cachedAt='{}', timeToLive='{}'")
                .addArgument(cacheKey)
                .addArgument(cacheData.data())
                .addArgument(cacheData.cachedAt())
                .addArgument(cacheData.timeToLive())
                .log();

        cache.put(cacheKey, cacheData);
        logCachePutSuccess(CacheAction.PUT, cacheKey);
    }

    @Override
    public void putIfAbsent(K cacheKey, V cacheValue) {
        validateCacheValue(cacheValue);
        putIfAbsent(cacheKey, () -> cacheValue, defaultTimeToLive);
    }

    @Override
    public void putIfAbsent(K cacheKey, V cacheValue, Duration timeToLive) {
        validateCacheValue(cacheValue);
        putIfAbsent(cacheKey, () -> cacheValue, timeToLive);
    }

    @Override
    public void putIfAbsent(K cacheKey, Supplier<V> valueSupplier) {
        putIfAbsent(cacheKey, valueSupplier, defaultTimeToLive);
    }

    @Override
    public void putIfAbsent(K cacheKey, Supplier<V> valueSupplier, Duration timeToLive) {
        validateCacheKey(cacheKey);
        validateValueSupplier(valueSupplier);
        validateCacheEntryTimeToLive(timeToLive);

        var wrapper = cache.get(cacheKey);

        if (wrapper == null || wrapper.get() == null) {
            V value = valueSupplier.get();
            validateCacheValue(value); // avoid putting null from supplier

            CacheData<V> wrapped = CacheData.of(value, timeToLive, clock);

            LOGGER.atDebug()
                    .setMessage("putIfAbsent: key='{}' not present. TTL={}, storing new value from supplier.")
                    .addArgument(cacheKey)
                    .addArgument(timeToLive)
                    .log();

            cache.put(cacheKey, wrapped);
            logCachePutSuccess(CacheAction.PUT_IF_ABSENT, cacheKey);
        }
        else {
            LOGGER.atDebug()
                    .setMessage("putIfAbsent: key='{}' already present. Skipping put.")
                    .addArgument(cacheKey)
                    .log();
        }
    }

    @Override
    public void putIfAbsentOrStale(K cacheKey, V cacheValue) {
        validateCacheValue(cacheValue);
        putIfAbsentOrStale(cacheKey, () -> cacheValue, defaultTimeToLive);
    }

    @Override
    public void putIfAbsentOrStale(K cacheKey, V cacheValue, Duration timeToLive) {
        validateCacheValue(cacheValue);
        putIfAbsentOrStale(cacheKey, () -> cacheValue, timeToLive);
    }

    @Override
    public void putIfAbsentOrStale(K cacheKey, Supplier<V> valueSupplier) {
        putIfAbsentOrStale(cacheKey, valueSupplier, defaultTimeToLive);
    }

    @Override
    public void putIfAbsentOrStale(K cacheKey, Supplier<V> valueSupplier, Duration timeToLive) {
        validateCacheKey(cacheKey);
        validateValueSupplier(valueSupplier);
        validateCacheEntryTimeToLive(timeToLive);

        var wrapper = cache.get(cacheKey);

        if (wrapper == null) {
            V value = valueSupplier.get();
            validateCacheValue(value); // avoid storing null from supplier

            CacheData<V> newEntry = CacheData.of(value, timeToLive, clock);

            LOGGER.atDebug()
                    .setMessage("putIfAbsentOrStale: key='{}' not present. Storing value from supplier.")
                    .addArgument(cacheKey)
                    .log();

            cache.put(cacheKey, newEntry);
            logCachePutSuccess(CacheAction.PUT_IF_ABSENT_OR_STALE, cacheKey);
            return;
        }

        @SuppressWarnings("unchecked")
        CacheData<V> existing = (CacheData<V>) wrapper.get();

        if (existing == null || existing.hasExpired(clock)) {
            V value = valueSupplier.get();
            validateCacheValue(value);

            CacheData<V> newEntry = CacheData.of(value, timeToLive, clock);

            LOGGER.atDebug()
                    .setMessage("putIfAbsentOrStale: key='{}' is stale or null. Replacing with value from supplier.")
                    .addArgument(cacheKey)
                    .log();

            cache.put(cacheKey, newEntry);
            logCachePutSuccess(CacheAction.PUT_IF_ABSENT_OR_STALE, cacheKey);
        } else {
            LOGGER.atDebug()
                    .setMessage("putIfAbsentOrStale: key='{}' is fresh. Skipping supplier execution.")
                    .addArgument(cacheKey)
                    .log();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<V> getIfPresent(K cacheKey) {
        validateCacheKey(cacheKey);

        var wrapper = cache.get(cacheKey);
        if (wrapper != null) {
            V value = (V) wrapper.get();
            if (value != null) {
                LOGGER.debug("Cache getIfPresent: key='{}' hit -> value={}", cacheKey, value);
                return Optional.of(value);
            }
        }

        LOGGER.info("Cache getIfPresent: key='{}' miss.", cacheKey);
        return Optional.empty();
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

    private void logCachePutSuccess(CacheAction action, K key) {
        LOGGER.atInfo()
                .setMessage("Cache {}: key='{}' successfully stored in cache.")
                .addArgument(action)
                .addArgument(key)
                .log();
    }
}
