package com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients;

import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.BankAccountDTO;

import java.util.Optional;

@FunctionalInterface
public interface BankAccountLookupPort {

    Optional<BankAccountDTO> findBankAccountByIban(String bankAccountIban);
}