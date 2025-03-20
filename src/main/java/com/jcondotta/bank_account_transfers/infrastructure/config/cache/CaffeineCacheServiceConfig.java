package com.jcondotta.bank_account_transfers.infrastructure.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.BankAccountDTO;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache.CaffeineCacheStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineCacheServiceConfig {

    @Bean
    public CacheStore<String, BankAccountDTO> bankAccountCacheService(Cache<String, BankAccountDTO> cache) {
        return new CaffeineCacheStore<>(cache);
    }
}