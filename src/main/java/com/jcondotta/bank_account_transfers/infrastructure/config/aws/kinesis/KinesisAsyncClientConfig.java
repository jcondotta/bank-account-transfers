package com.jcondotta.bank_account_transfers.infrastructure.config.aws.kinesis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

import java.net.URI;

@Configuration
public class KinesisAsyncClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KinesisAsyncClientConfig.class);

    @Bean
    @ConditionalOnProperty(name = "aws.kinesis.endpoint")
    public KinesisAsyncClient kinesisAsyncClientLocal(AwsCredentialsProvider awsCredentialsProvider, Region region,
                                             @Value("${aws.kinesis.endpoint}") String endpoint) {

        LOGGER.atInfo()
                .setMessage("Initializing KinesisAsyncClient with custom endpoint: {}")
                .addArgument(endpoint)
                .log();

        return KinesisAsyncClient.builder()
                .region(region)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(KinesisAsyncClient.class)
    public KinesisAsyncClient kinesisAsyncClient(AwsCredentialsProvider awsCredentialsProvider, Region region) {
        return KinesisAsyncClient.builder()
            .region(region)
            .credentialsProvider(awsCredentialsProvider)
            .build();
    }
}
