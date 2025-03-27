package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bank-account-service.api.v1")
public class BankAccountServiceV1Config implements BankAccountServiceConfig {

    private String baseURL;
    private String pathURL;

    protected BankAccountServiceV1Config() {}

    public BankAccountServiceV1Config(String baseURL, String pathURL) {
        this.baseURL = baseURL;
        this.pathURL = pathURL;
    }

    @Override
    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    @Override
    public String getPathURL() {
        return pathURL;
    }

    public void setPathURL(String pathURL) {
        this.pathURL = pathURL;
    }
}
