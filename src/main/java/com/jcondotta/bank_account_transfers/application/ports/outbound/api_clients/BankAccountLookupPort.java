package com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients;

import java.util.Optional;

@FunctionalInterface
public interface BankAccountLookupPort<K, V> {

    Optional<V> findBankAccount(K filterParam);
}