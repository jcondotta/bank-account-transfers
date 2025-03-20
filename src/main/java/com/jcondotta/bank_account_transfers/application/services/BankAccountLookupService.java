package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import com.jcondotta.bank_account_transfers.application.usecases.BankAccountLookupUseCase;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.BankAccountDTO;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache.BankAccountCacheKeys;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountLookupService implements BankAccountLookupUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountLookupService.class);

    private final CacheStore<String, BankAccountDTO> cacheStore;
    private final BankAccountLookupPort<BankAccountDTO> bankAccountLookupPort;

    public BankAccountLookupService(CacheStore<String, BankAccountDTO> cacheStore, BankAccountLookupPort<BankAccountDTO> bankAccountLookupPort) {
        this.cacheStore = cacheStore;
        this.bankAccountLookupPort = bankAccountLookupPort;
    }

    @Override
    @Timed(value = "bankAccounts.findBankAccountByIban.time",
            description = "Time taken to find a bank account by iban",
            percentiles = {0.5, 0.9, 0.99},
            extraTags = "bankAccounts")
    public Optional<BankAccountDTO> findBankAccountByIban(String bankAccountIban) {
        var cacheKey = BankAccountCacheKeys.bankAccountIbanKey(bankAccountIban);

        LOGGER.atInfo().setMessage("Checking cache by bank account iban: {}")
                .addArgument(bankAccountIban)
                .addKeyValue("bankAccountIban", bankAccountIban).log();

        return cacheStore.getOrFetch(cacheKey, key -> bankAccountLookupPort.findBankAccountByIban(bankAccountIban));
    }
}