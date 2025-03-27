package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public interface BankAccountServiceConfig {

    String getBaseURL();
    String getPathURL();

    default URI findBankAccountByIbanURI(String bankAccountIban){
        return UriComponentsBuilder.fromUriString(getBaseURL())
                .path(getPathURL())
                .buildAndExpand(bankAccountIban)
                .toUri();
    }
}