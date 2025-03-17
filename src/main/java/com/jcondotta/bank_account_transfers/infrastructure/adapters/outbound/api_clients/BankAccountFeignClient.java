package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;

//@FeignClient(name = "bankAccountService", url = "http://bank-account-service")
public interface BankAccountFeignClient extends BankAccountLookupPort {

//    @Override
//    @GetMapping("/accounts/lookup")
//    Optional<UUID> findBankAccountByIban(@RequestParam("iban") String bankAccountIban);
}
