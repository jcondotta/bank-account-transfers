package com.jcondotta.bank_account_transfers.infrastructure.facade.lookup_bank_account;

import com.jcondotta.bank_account_transfers.domain.bank_account.exceptions.BankAccountNotFoundException;
import com.jcondotta.bank_account_transfers.domain.bank_account.model.BankAccount;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.client.lookup_bank_account.LookupBankAccountClient;
import com.jcondotta.bank_account_transfers.infrastructure.facade.lookup_bank_account.mapper.LookupBankAccountCdoFacadeMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LookupBankAccountFacadeImpl implements LookupBankAccountFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupBankAccountFacadeImpl.class);

    private final LookupBankAccountClient lookupBankAccountClient;
    private final LookupBankAccountCdoFacadeMapper lookupBankAccountFacadeMapper;

    @Override
    public BankAccount byId(BankAccountId bankAccountId) {
        try {
            LOGGER.debug("Attempting to fetch bank account with id: {}", bankAccountId);
            var response = lookupBankAccountClient.findById(bankAccountId.value());
            return lookupBankAccountFacadeMapper.map(response.bankAccountCdo());
        }
        catch (FeignException.NotFound exception) {
            throw new BankAccountNotFoundException(bankAccountId);
        }
        catch (FeignException.InternalServerError exception) {
            LOGGER.error("Internal server error while fetching bank account with ID: {}. Reason: {}", bankAccountId, exception.getMessage(), exception);
            throw new RuntimeException("Failed to fetch bank account due to internal server error", exception);
        }
        catch (FeignException exception) {
            LOGGER.error("Unexpected FeignException while fetching bank account with ID: {}. Status: {}, Message: {}",
                bankAccountId, exception.status(), exception.getMessage(), exception);
            throw new RuntimeException("Unexpected error while fetching bank account", exception);
        }
    }

    @Override
    public BankAccount byIban(Iban iban) {
        try {
            LOGGER.debug("Attempting to fetch bank account with iban: {}", iban);
            var response = lookupBankAccountClient.findByIban(iban.value());
            return lookupBankAccountFacadeMapper.map(response.bankAccountCdo());
        }
        catch (FeignException.NotFound exception) {
            throw new BankAccountNotFoundException(iban);
        }
        catch (FeignException.InternalServerError exception) {
            LOGGER.error("Internal server error while fetching bank account with Iban: {}. Reason: {}", iban, exception.getMessage(), exception);
            throw new RuntimeException("Failed to fetch bank account due to internal server error", exception);
        }
        catch (FeignException exception) {
            LOGGER.error("Unexpected FeignException while fetching bank account with ID: {}. Status: {}, Message: {}",
                iban, exception.status(), exception.getMessage(), exception);
            throw new RuntimeException("Unexpected error while fetching bank account", exception);
        }
    }
}
