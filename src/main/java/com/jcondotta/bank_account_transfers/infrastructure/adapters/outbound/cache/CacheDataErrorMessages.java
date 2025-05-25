package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

public interface CacheDataErrorMessages {

    String DATA_MUST_NOT_BE_NULL = "cacheData.data.notNull";
    String CACHED_AT_MUST_NOT_BE_NULL = "cacheData.cachedAt.notNull";
    String TTL_MUST_NOT_BE_NULL = "cacheData.timeToLive.notNull";
    String CLOCK_MUST_NOT_BE_NULL = "cacheData.clock.notNull";

}
