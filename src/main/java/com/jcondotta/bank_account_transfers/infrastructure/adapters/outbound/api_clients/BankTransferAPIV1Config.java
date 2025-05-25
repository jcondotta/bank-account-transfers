package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.v1.bank-transfers")
public class BankTransferAPIV1Config implements BankTransferAPIConfig {

    private String rootPath;

    protected BankTransferAPIV1Config() {}

    public BankTransferAPIV1Config(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
