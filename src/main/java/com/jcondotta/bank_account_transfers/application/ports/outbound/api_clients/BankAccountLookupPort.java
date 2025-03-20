package com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients;

import java.util.Optional;

@FunctionalInterface
public interface BankAccountLookupPort<T> {

    Optional<T> findBankAccountByIban(String bankAccountIban);
}