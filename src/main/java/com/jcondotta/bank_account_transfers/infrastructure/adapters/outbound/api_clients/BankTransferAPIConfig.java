package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

public interface BankTransferAPIConfig {

    String getRootPath();
//    String getPathURL();
//
//    default URI findBankAccountByIbanURI(String bankAccountIban){
//        return UriComponentsBuilder.fromUriString(getBaseURL())
//                .path(getPathURL())
//                .buildAndExpand(bankAccountIban)
//                .toUri();
//    }
}