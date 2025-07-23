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
import software.amazon.awssdk.services.kinesis.KinesisClient;

import java.net.URI;

@Configuration
public class KinesisClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KinesisClientConfig.class);

    @Bean
    @ConditionalOnProperty(name = "aws.kinesis.endpoint")
    public KinesisClient kinesisClientLocal(AwsCredentialsProvider awsCredentialsProvider, Region region,
                                             @Value("${aws.kinesis.endpoint}") String endpoint) {

        LOGGER.atInfo()
                .setMessage("Initializing KinesisClient with custom endpoint: {}")
                .addArgument(endpoint)
                .log();

        return KinesisClient.builder()
                .region(region)
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(KinesisClient.class)
    public KinesisClient kinesisClient(AwsCredentialsProvider awsCredentialsProvider, Region region) {
        return KinesisClient.builder()
            .region(region)
            .credentialsProvider(awsCredentialsProvider)
            .build();
    }
}
