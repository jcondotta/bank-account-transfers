package com.jcondotta.bank_account_transfers.infrastructure.config.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankTransferMapperConfig {

    @Bean
    public BankTransferMapper bankTransferMapper() {
        return BankTransferMapper.INSTANCE;
    }
}
