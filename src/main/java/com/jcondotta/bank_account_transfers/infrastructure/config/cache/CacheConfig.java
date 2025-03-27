package com.jcondotta.bank_account_transfers.infrastructure.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts.BankAccountDTO;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache.CaffeineCacheStore;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("bankAccountCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());
        return cacheManager;
    }

    @Bean
    public CacheStore<String, BankAccountDTO> bankAccountCacheService(CacheManager cacheManager) {
        org.springframework.cache.Cache springCache = cacheManager.getCache("bankAccountCache");
        return new CaffeineCacheStore<>(springCache);
    }
}