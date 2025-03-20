package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import com.jcondotta.bank_account_transfers.application.usecases.BankAccountLookupUseCase;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.BankAccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BankAccountLookupServiceTest {

    private BankAccountLookupUseCase bankAccountLookupUseCase;

    @Mock
    private CacheStore<String, BankAccountDTO> cacheStore;

    @Mock
    private BankAccountLookupPort<BankAccountDTO> bankAccountLookupPort;


    @BeforeEach
    void beforeEach() throws IOException {
        bankAccountLookupUseCase = new BankAccountLookupService(cacheStore, bankAccountLookupPort);
    }

    @Test
    public void teste(){
        bankAccountLookupUseCase.findBankAccountByIban(UUID.randomUUID().toString());
    }
}