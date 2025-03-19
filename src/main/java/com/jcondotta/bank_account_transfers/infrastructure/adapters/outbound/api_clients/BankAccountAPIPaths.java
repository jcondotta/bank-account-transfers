package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public interface BankAccountAPIPaths {

    static URI findBankAccountByIbanURI(String findBankAccountByIbanURL, String bankAccountIban) {
        return UriComponentsBuilder
                .fromUriString(findBankAccountByIbanURL)
                .buildAndExpand(bankAccountIban)
                .toUri();
    }
}
