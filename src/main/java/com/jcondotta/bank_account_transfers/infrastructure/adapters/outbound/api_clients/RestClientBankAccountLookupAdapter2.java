package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts.AccountHolderDTO;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts.BankAccountDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Primary
public class RestClientBankAccountLookupAdapter2 implements BankAccountLookupPort<String, BankAccountDTO> {

    @Override
    public Optional<BankAccountDTO> findBankAccount(String bankAccountIban) {
        AccountHolderDTO jeffersonAccountHolder = new AccountHolderDTO(UUID.randomUUID(), "Jefferson Condotta", "Primary");
        BankAccountDTO bankAccountDTO = new BankAccountDTO(UUID.randomUUID(), bankAccountIban, List.of(jeffersonAccountHolder));
        return Optional.of(bankAccountDTO);
    }
}
