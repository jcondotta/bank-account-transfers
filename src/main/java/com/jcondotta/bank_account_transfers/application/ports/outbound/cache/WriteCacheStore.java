package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

public interface WriteCacheStore<K, V> {

    void set(K cacheKey, V cacheValue);

    //TODO
//    default void invalidate(K cacheKey){
//
//    }
}
