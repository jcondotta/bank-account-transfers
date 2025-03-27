package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import com.jcondotta.bank_account_transfers.application.ports.outbound.cache.CacheStore;
import com.jcondotta.bank_account_transfers.application.usecases.BankAccountLookupUseCase;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts.BankAccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountLookupServiceTest {

    private static final String BANK_ACCOUNT_IBAN_JEFFERSON = TestBankAccount.JEFFERSON.getIban();

    @Mock
    private CacheStore<String, BankAccountDTO> cacheStore;

    @Mock
    private BankAccountLookupPort<String, BankAccountDTO> bankAccountLookupPort;

    @Mock
    private BankAccountDTO bankAccountDTO;

    private BankAccountLookupUseCase bankAccountLookupUseCase;

    @BeforeEach
    void beforeEach() {
        bankAccountLookupUseCase = new BankAccountLookupService(cacheStore, bankAccountLookupPort);
    }

    @Test
    void shouldReturnCachedBankAccountDTO_whenCacheEntryExists() {
        when(cacheStore.getOrFetch(anyString(), ArgumentMatchers.<Supplier<Optional<BankAccountDTO>>>any()))
                .thenReturn(Optional.of(bankAccountDTO));

        bankAccountLookupUseCase.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);

        verify(cacheStore).getOrFetch(anyString(), ArgumentMatchers.<Supplier<Optional<BankAccountDTO>>>any());
        verify(bankAccountLookupPort, never()).findBankAccount(anyString());
    }

    @Test
    void shouldFetchFromAPIAndSetResultInCache_whenCacheMissHappens() {
        when(cacheStore.getOrFetch(anyString(), ArgumentMatchers.<Supplier<Optional<BankAccountDTO>>>any()))
                .thenAnswer(invocation -> {
                    Supplier<Optional<BankAccountDTO>> loader = invocation.getArgument(1);
                    return loader.get();
                });

        when(bankAccountLookupPort.findBankAccount(BANK_ACCOUNT_IBAN_JEFFERSON))
                .thenReturn(Optional.of(bankAccountDTO));

        bankAccountLookupUseCase.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);

        verify(cacheStore).getOrFetch(anyString(), ArgumentMatchers.<Supplier<Optional<BankAccountDTO>>>any());
        verify(bankAccountLookupPort).findBankAccount(anyString());
    }

    @Test
    void shouldReturnEmptyOptional_whenBankAccountAPIFails() {
        when(cacheStore.getOrFetch(anyString(), ArgumentMatchers.<Supplier<Optional<BankAccountDTO>>>any()))
                .thenAnswer(invocation -> {
                    Supplier<Optional<BankAccountDTO>> loader = invocation.getArgument(1);
                    return loader.get();
                });

        when(bankAccountLookupPort.findBankAccount(BANK_ACCOUNT_IBAN_JEFFERSON))
                .thenReturn(Optional.empty());

        bankAccountLookupUseCase.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);

        verify(cacheStore).getOrFetch(anyString(), ArgumentMatchers.<Supplier<Optional<BankAccountDTO>>>any());
        verify(bankAccountLookupPort).findBankAccount(anyString());
    }
}