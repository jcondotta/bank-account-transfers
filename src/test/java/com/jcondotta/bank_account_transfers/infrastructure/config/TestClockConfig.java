package com.jcondotta.bank_account_transfers.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@Configuration
public class TestClockConfig {

    public static final Clock testFixedClock = Clock.fixed(Instant.parse("2022-06-24T12:45:01Z"), ZoneOffset.UTC);

    @Bean
    @Primary
    public Clock testFixedClock(){
        return testFixedClock;
    }
}