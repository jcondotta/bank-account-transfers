package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface BankAccountAPIPaths {

    static URI findBankAccountByIbanURI(String findBankAccountByIbanURL, String bankAccountIban) {
        return UriComponentsBuilder
                .fromUriString(findBankAccountByIbanURL)
                .buildAndExpand(bankAccountIban)
                .toUri();
    }
}
