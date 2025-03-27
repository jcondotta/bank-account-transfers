package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.application.usecases.CreateTransactionUseCase;
import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryMovement;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction.CreateTransactionRequest;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.mapper.TransactionMapper;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestClockConfig;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestValidatorConfig;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import static com.jcondotta.bank_account_transfers.ValidationTestHelper.assertSingleViolation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith(MockitoExtension.class)
class CreateTransactionServiceTest {

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";

    private static final MonetaryAmount MONETARY_AMOUNT_200_EUR = MonetaryAmount.of(AMOUNT_200, CURRENCY_EURO);

    private static final String TRANSFER_REFERENCE = "Invoice 437263";

    private static final Clock TEST_FIXED_CLOCK = TestClockConfig.testFixedClock;
    private static final Validator VALIDATOR = TestValidatorConfig.getValidator();
    private static final TransactionMapper TRANSACTION_MAPPER = TransactionMapper.INSTANCE;

    private CreateTransactionUseCase createTransactionUseCase;

    @BeforeEach
    void beforeEach() {
        createTransactionUseCase = new CreateTransactionService(TRANSACTION_MAPPER, VALIDATOR, TEST_FIXED_CLOCK);
    }

    @ParameterizedTest(name = "[VALID] Should create transaction with transactionType={0}")
    @EnumSource(TransactionType.class)
    void shouldCreateTransaction_whenRequestIsValid(TransactionType transactionType) {
        var monetaryMovement = MonetaryMovement.of(transactionType, MONETARY_AMOUNT_200_EUR);
        var request = CreateTransactionRequest.builder()
                .bankAccountId(BANK_ACCOUNT_ID_JEFFERSON)
                .monetaryMovement(monetaryMovement)
                .reference(TRANSFER_REFERENCE)
                .build();

        var transaction = createTransactionUseCase.createTransaction(request);

        assertAll(
                () -> assertThat(transaction.getTransactionId()).isNull(),
                () -> assertThat(transaction.getBankAccountId()).isEqualTo(request.bankAccountId()),
                () -> assertThat(transaction.getTransactionType()).isEqualTo(request.monetaryMovement().transactionType()),
                () -> assertThat(transaction.getAmount()).isEqualTo(request.monetaryMovement().amount()),
                () -> assertThat(transaction.getCurrency()).isEqualTo(request.monetaryMovement().currency()),
                () -> assertThat(transaction.getReference()).isEqualTo(request.reference()),
                () -> assertThat(transaction.getCreatedAt()).isEqualTo(Instant.now(TEST_FIXED_CLOCK))
        );
    }

    @ParameterizedTest(name = "[INVALID] Should fail when bank account id is null for transactionType={0}")
    @EnumSource(TransactionType.class)
    void shouldThrowConstraintViolationException_whenBankAccountIdIsNull(TransactionType transactionType) {
        var monetaryMovement = MonetaryMovement.of(transactionType, MONETARY_AMOUNT_200_EUR);
        var request = CreateTransactionRequest.builder()
                .bankAccountId(null)
                .monetaryMovement(monetaryMovement)
                .reference(TRANSFER_REFERENCE)
                .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                createTransactionUseCase.createTransaction(request)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "transaction.bankAccountId.notNull", "bankAccountId");
    }

    @Test
    void shouldThrowConstraintViolationException_whenMonetaryMovementIsNull() {
        var request = CreateTransactionRequest.builder()
                .bankAccountId(BANK_ACCOUNT_ID_JEFFERSON)
                .monetaryMovement(null)
                .reference(TRANSFER_REFERENCE)
                .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                createTransactionUseCase.createTransaction(request)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "transaction.monetaryMovement.notNull", "monetaryMovement");
    }

    @ParameterizedTest(name = "[INVALID] Should fail when reference exceeds 50 chars for transactionType={0}")
    @EnumSource(TransactionType.class)
    void shouldThrowConstraintViolationException_whenReferenceExceeds50Characters(TransactionType transactionType) {
        var monetaryMovement = MonetaryMovement.of(transactionType, MONETARY_AMOUNT_200_EUR);
        var tooLongReference = "J".repeat(51);

        var request = CreateTransactionRequest.builder()
                .bankAccountId(BANK_ACCOUNT_ID_JEFFERSON)
                .monetaryMovement(monetaryMovement)
                .reference(tooLongReference)
                .build();

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                createTransactionUseCase.createTransaction(request)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "transaction.reference.tooLong", "reference");
    }
}