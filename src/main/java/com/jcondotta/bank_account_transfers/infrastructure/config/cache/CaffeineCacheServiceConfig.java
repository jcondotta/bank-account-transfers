package com.jcondotta.bank_account_transfers.infrastructure.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.BankAccountDTO;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheService;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache.CaffeineCacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineCacheServiceConfig {

    @Bean
    public CacheService<String, BankAccountDTO> bankAccountCacheService(Cache<String, BankAccountDTO> cache) {
        return new CaffeineCacheService<>(cache);
    }
}