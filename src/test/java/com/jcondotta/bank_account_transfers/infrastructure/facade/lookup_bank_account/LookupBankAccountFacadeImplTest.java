package com.jcondotta.bank_account_transfers.infrastructure.facade.lookup_bank_account;

import com.jcondotta.bank_account_transfers.application.TestAccountDetails;
import com.jcondotta.bank_account_transfers.argument_provider.AccountTypeStatusAndCurrencyArgumentsProvider;
import com.jcondotta.bank_account_transfers.domain.bank_account.enums.AccountStatus;
import com.jcondotta.bank_account_transfers.domain.bank_account.enums.AccountType;
import com.jcondotta.bank_account_transfers.domain.bank_account.exceptions.BankAccountNotFoundException;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.client.lookup_bank_account.LookupBankAccountClient;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.client.lookup_bank_account.model.BankAccountCdo;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.client.lookup_bank_account.model.LookupBankAccountResponseCdo;
import com.jcondotta.bank_account_transfers.infrastructure.facade.lookup_bank_account.mapper.LookupBankAccountCdoFacadeMapper;
import feign.FeignException;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LookupBankAccountFacadeImplTest {

    @Mock
    private LookupBankAccountClient client;

    private final LookupBankAccountCdoFacadeMapper mapper = LookupBankAccountCdoFacadeMapper.INSTANCE;

    private LookupBankAccountFacade facade;

    private final BankAccountId bankAccountId = BankAccountId.newId();
    private final Iban iban = Iban.of(TestAccountDetails.JEFFERSON.getIban());

    @BeforeEach
    void setUp() {
        facade = new LookupBankAccountFacadeImpl(client, mapper);
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeStatusAndCurrencyArgumentsProvider.class)
    void shouldReturnBankAccount_whenAccountIsFoundById(AccountType accountType, AccountStatus accountStatus, Currency currency) {
        var bankAccountCdo = Instancio.of(BankAccountCdo.class)
            .set(field("accountType"), accountType.name())
            .set(field("status"), accountStatus.name())
            .set(field("currency"), currency.name())
            .set(field("iban"), iban.value())
            .create();

        var responseCdo = new LookupBankAccountResponseCdo(bankAccountCdo);

        when(client.findById(bankAccountId.value())).thenReturn(responseCdo);

        assertThat(facade.byId(bankAccountId))
            .satisfies(bankAccount -> {
                assertThat(bankAccount.bankAccountId().value()).isEqualTo(bankAccountCdo.bankAccountId());
                assertThat(bankAccount.iban().value()).isEqualTo(bankAccountCdo.iban());
                assertThat(bankAccount.accountType().name()).isEqualTo(bankAccountCdo.accountType());
                assertThat(bankAccount.currency().name()).isEqualTo(bankAccountCdo.currency());
                assertThat(bankAccount.status().name()).isEqualTo(bankAccountCdo.status());
                assertThat(bankAccount.createdAt().value()).isEqualTo(bankAccountCdo.dateOfOpening().toInstant());
                assertThat(bankAccount.createdAt().zoneId()).isEqualTo(bankAccountCdo.dateOfOpening().getZone());
            });
    }

    @Test
    void shouldThrowBankAccountNotFoundException_whenAccountIsNotFoundById() {
        when(client.findById(bankAccountId.value()))
            .thenThrow(mock(FeignException.NotFound.class));

        assertThatThrownBy(() -> facade.byId(bankAccountId))
            .isInstanceOf(BankAccountNotFoundException.class)
            .hasMessage("bankAccount.notFound");
    }

    @Test
    void shouldThrowRuntimeException_whenInternalServerErrorById() {
        var exception = mock(FeignException.InternalServerError.class);
        when(client.findById(bankAccountId.value())).thenThrow(exception);

        assertThatThrownBy(() -> facade.byId(bankAccountId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("internal server error");
    }

    @Test
    void shouldThrowRuntimeException_whenUnexpectedFeignExceptionById() {
        var exception = mock(FeignException.class);
        when(client.findById(bankAccountId.value())).thenThrow(exception);

        assertThatThrownBy(() -> facade.byId(bankAccountId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Unexpected error");
    }

    @ParameterizedTest
    @ArgumentsSource(AccountTypeStatusAndCurrencyArgumentsProvider.class)
    void shouldReturnBankAccount_whenAccountIsFoundByIban(AccountType accountType, AccountStatus accountStatus, Currency currency) {
        var bankAccountCdo = Instancio.of(BankAccountCdo.class)
            .set(field("accountType"), accountType.name())
            .set(field("status"), accountStatus.name())
            .set(field("currency"), currency.name())
            .set(field("iban"), iban.value())
            .create();

        var responseCdo = new LookupBankAccountResponseCdo(bankAccountCdo);

        when(client.findByIban(iban.value())).thenReturn(responseCdo);

        assertThat(facade.byIban(iban))
            .satisfies(bankAccount -> {
                assertThat(bankAccount).isNotNull();
                assertThat(bankAccount.bankAccountId().value()).isEqualTo(bankAccountCdo.bankAccountId());
                assertThat(bankAccount.iban().value()).isEqualTo(bankAccountCdo.iban());
                assertThat(bankAccount.accountType().name()).isEqualTo(bankAccountCdo.accountType());
                assertThat(bankAccount.currency().name()).isEqualTo(bankAccountCdo.currency());
                assertThat(bankAccount.status().name()).isEqualTo(bankAccountCdo.status());
                assertThat(bankAccount.createdAt().value()).isEqualTo(bankAccountCdo.dateOfOpening().toInstant());
                assertThat(bankAccount.createdAt().zoneId()).isEqualTo(bankAccountCdo.dateOfOpening().getZone());
            });
    }


    @Test
    void shouldThrowBankAccountNotFoundException_whenNotFoundByIban() {
        when(client.findByIban(iban.value()))
            .thenThrow(mock(FeignException.NotFound.class));

        assertThatThrownBy(() -> facade.byIban(iban))
            .isInstanceOf(BankAccountNotFoundException.class)
            .hasMessage("bankAccount.notFound");
    }

    @Test
    void shouldThrowRuntimeException_whenInternalServerErrorByIban() {
        var exception = mock(FeignException.InternalServerError.class);
        when(client.findByIban(iban.value())).thenThrow(exception);

        assertThatThrownBy(() -> facade.byIban(iban))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("internal server error");
    }

    @Test
    void shouldThrowRuntimeException_whenUnexpectedFeignExceptionByIban() {
        var exception = mock(FeignException.class);
        when(client.findByIban(iban.value())).thenThrow(exception);

        assertThatThrownBy(() -> facade.byIban(iban))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Unexpected error");
    }
}
