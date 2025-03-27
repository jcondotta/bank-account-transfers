package com.jcondotta.bank_account_transfers.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;

@TestConfiguration
public class PostgresTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresTestContainer.class);

    private static final String POSTGRES_IMAGE_NAME = "postgres:16-alpine";

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>(POSTGRES_IMAGE_NAME)
            .withDatabaseName("bank_account_transfers_test_db")
            .withUsername("testuser")
            .withPassword("testpass");
//            .withReuse(true);

    static {
        Startables.deepStart(POSTGRESQL_CONTAINER).join();
    }

    private static Map<String, String> getContainerProperties() {
        return Map.of(
                "spring.datasource.url", POSTGRESQL_CONTAINER.getJdbcUrl(),
                "spring.datasource.username", POSTGRESQL_CONTAINER.getUsername(),
                "spring.datasource.password", POSTGRESQL_CONTAINER.getPassword(),
                "spring.datasource.driver-class-name", "org.postgresql.Driver",
                "spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect"
        );
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(getContainerProperties()).applyTo(applicationContext.getEnvironment());
    }
}
