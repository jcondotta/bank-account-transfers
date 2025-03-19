package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Primary
public class RestClientBankAccountLookupAdapter2 implements BankAccountLookupPort {


    @Override
    @Timed(value = "bankAccount.findBankAccountByIban.time", description = "Time taken to fetch a bank account by IBAN",
            percentiles = {0.5, 0.9, 0.99})
    public Optional<BankAccountDTO> findBankAccountByIban(@NotNull(message = "bankAccount.iban.notNull") String bankAccountIban) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO(UUID.randomUUID(), bankAccountIban);
        return Optional.of(bankAccountDTO);
    }
}
