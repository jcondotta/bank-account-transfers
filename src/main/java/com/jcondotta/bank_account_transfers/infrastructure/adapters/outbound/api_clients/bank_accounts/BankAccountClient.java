package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bankAccountClient", url = "${bank-account-service.api.v1.base-url}")
public interface BankAccountClient {

    @GetMapping(value = "${bank-account-service.api.v1.path-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    BankAccountDTO findBankAccountByIban(@PathVariable("iban") String iban);
}
