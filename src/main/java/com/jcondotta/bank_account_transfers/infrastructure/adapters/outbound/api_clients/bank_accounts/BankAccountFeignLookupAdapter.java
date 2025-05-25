package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts;

import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import feign.FeignException;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class BankAccountFeignLookupAdapter implements BankAccountLookupPort<String, BankAccountDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountFeignLookupAdapter.class);

    private final BankAccountClient bankAccountClient;

    public BankAccountFeignLookupAdapter(BankAccountClient bankAccountClient) {
        this.bankAccountClient = bankAccountClient;
    }

    @Override
    public Optional<BankAccountDTO> findBankAccount(String bankAccountIban) {
        try {
            LOGGER.info("Fetching by bank account iban: {}", bankAccountIban);

            var bankAccountDTO = bankAccountClient.findBankAccountByIban(bankAccountIban);

            LOGGER.warn("Bank account successfully retrieved: {}", StructuredArguments.f(bankAccountDTO));
            return Optional.of(bankAccountDTO);
        }
        catch (FeignException.NotFound e) {
            LOGGER.warn("Bank account not found for IBAN: {}", bankAccountIban);
            return Optional.empty();
        }
        catch (FeignException e) {
            LOGGER.error("Bank account lookup failed: {} {} {}", 
                e.status(), bankAccountIban, e.getMessage(), e);
        }
        catch (Exception e) {
            LOGGER.error("Unexpected error during lookup by bank account iban: {}", bankAccountIban, e);
        }

        return Optional.empty();
    }
}
