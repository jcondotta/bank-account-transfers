package com.jcondotta.bank_account_transfers.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "bank-account-service.api.v1")
public class BankAccountServiceV1Config {

    private String baseUrl;
    private FindByIban findByIban; // ✅ Nested class matches YAML structure

    public static class FindByIban {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = Objects.requireNonNull(url, "Find by IBAN URL cannot be null");
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "Base URL cannot be null");
    }

    public FindByIban getFindByIban() {
        return findByIban;
    }

    public void setFindByIban(FindByIban findByIban) {
        this.findByIban = findByIban;
    }

    public URI findBankAccountByIbanURI(String bankAccountIban) {
        Objects.requireNonNull(bankAccountIban, "Bank account IBAN cannot be null");

        return UriComponentsBuilder.fromUriString(baseUrl)
                .path(findByIban.url)
                .buildAndExpand(bankAccountIban)
                .toUri();
    }
}
