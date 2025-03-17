package com.jcondotta.bank_account_transfers.application.usecases;

import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.BankAccountDTO;

import java.util.Optional;

public interface BankAccountLookupUseCase {
    Optional<BankAccountDTO> findBankAccountByIban(String recipientIban);
}
