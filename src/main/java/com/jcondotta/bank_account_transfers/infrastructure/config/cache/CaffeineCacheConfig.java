package com.jcondotta.bank_account_transfers.infrastructure.config.cache;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineCacheConfig {

//    @Bean
//    public Cache<String, BankAccountDTO> bankAccountCache() {
//        return Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .recordStats()
//                .build();
//    }
}