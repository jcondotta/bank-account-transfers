package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.TestBankAccountDTO;
import com.jcondotta.bank_account_transfers.ValidationTestHelper;
import com.jcondotta.bank_account_transfers.application.ports.outbound.persistence.TransactionRepositoryPort;
import com.jcondotta.bank_account_transfers.application.usecases.BankAccountLookupUseCase;
import com.jcondotta.bank_account_transfers.application.usecases.CreateBankTransferUseCase;
import com.jcondotta.bank_account_transfers.application.usecases.CreateDoubleEntryTransactionUseCase;
import com.jcondotta.bank_account_transfers.application.usecases.CreateTransactionUseCase;
import com.jcondotta.bank_account_transfers.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryAmount;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.CreateBankTransferRequest;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestClockConfig;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestValidatorConfig;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static com.jcondotta.bank_account_transfers.ValidationTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBankTransferServiceTest {

    private static final UUID IDEMPOTENCY_KEY = UUID.fromString("8d4c6f5c-3726-4b44-94a7-29a016853fb1");

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();

    private static final String RECIPIENT_NAME_PATRIZIO = TestBankAccount.PATRIZIO.getAccountHolderName();
    private static final String RECIPIENT_IBAN_PATRIZIO = TestBankAccount.PATRIZIO.getIban();

    private static final BigDecimal TRANSFER_AMOUNT_TWO_HUNDRED = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";
    private static final String TRANSFER_REFERENCE = "INVOICE 877635";

    private static final Validator VALIDATOR = TestValidatorConfig.getValidator();

    @Mock
    private CreateDoubleEntryTransactionUseCase createDoubleEntryTransactionUseCase;

    @Mock
    private BankAccountLookupUseCase bankAccountLookupUseCase;

    private CreateBankTransferUseCase createBankTransferUseCase;

    @BeforeEach
    void beforeEach(){
        createBankTransferUseCase = new CreateBankTransferService(createDoubleEntryTransactionUseCase, bankAccountLookupUseCase, VALIDATOR);
    }

    @Test
    void shouldCreateBankTransfer_whenRequestIsValid(){
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        when(bankAccountLookupUseCase.findBankAccountByIban(anyString()))
                .thenReturn(Optional.of(TestBankAccountDTO.JEFFERSON.getBankAccountDTO()));

        createBankTransferUseCase.createBankTransfer(request);

        verify(bankAccountLookupUseCase).findBankAccountByIban(anyString());
        verify(createDoubleEntryTransactionUseCase).createDoubleEntryTransaction(
                any(UUID.class),
                any(UUID.class),
                any(MonetaryAmount.class),
                anyString()
        );
    }


    @Test
    void shouldThrowConstraintViolationException_whenRequestValidationFails() {
        var request = new CreateBankTransferRequest(
                null,
                null,
                null,
                null,
                "J".repeat(51)
        );

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                createBankTransferUseCase.createBankTransfer(request)
        );

        assertThat(exception.getConstraintViolations()).isNotEmpty();

        verifyNoInteractions(createDoubleEntryTransactionUseCase, bankAccountLookupUseCase);
    }

    @Test
    void shouldThrowBankAccountNotFoundException_whenBankAccountIsNotFound() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        when(bankAccountLookupUseCase.findBankAccountByIban(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> createBankTransferUseCase.createBankTransfer(request))
                .isInstanceOf(BankAccountNotFoundException.class)
                .hasMessage("bankAccount.notFound");

        verify(bankAccountLookupUseCase).findBankAccountByIban(anyString());
        verifyNoInteractions(createDoubleEntryTransactionUseCase);
    }
}