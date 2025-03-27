package com.jcondotta.bank_account_transfers.infrastructure.config.mapper;

import com.jcondotta.bank_account_transfers.infrastructure.adapters.mapper.TransactionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionMapperConfig {

    @Bean
    public TransactionMapper transactionMapper() {
        return TransactionMapper.INSTANCE;
    }
}
