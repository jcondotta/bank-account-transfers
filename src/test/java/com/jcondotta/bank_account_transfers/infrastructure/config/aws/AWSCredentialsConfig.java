package com.jcondotta.bank_account_transfers.infrastructure.config.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;

@Configuration
@EnableConfigurationProperties(AWSCredentialsConfig.AWSCredentialsProperties.class)
public class AWSCredentialsConfig {

    private static final Logger logger = LoggerFactory.getLogger(AWSCredentialsConfig.class);

    @Bean
    @ConditionalOnProperty(name = {"aws.access-key-id", "aws.secret-key"})
    public AwsCredentials awsCredentials(AWSCredentialsProperties properties) {
        logger.debug("Configuring AWS credentials with accessKeyId: {}", properties.accessKeyId());
        return AwsBasicCredentials.create(properties.accessKeyId(), properties.secretKey());
    }

    @ConfigurationProperties(prefix = "aws")
    public record AWSCredentialsProperties(String accessKeyId, String secretKey) {}
}
