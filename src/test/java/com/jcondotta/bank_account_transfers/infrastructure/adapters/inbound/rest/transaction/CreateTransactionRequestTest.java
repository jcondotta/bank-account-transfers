package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryMovement;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestValidatorConfig;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.util.UUID;

import static com.jcondotta.bank_account_transfers.ValidationTestHelper.assertSingleViolation;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class CreateTransactionRequestTest {

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";

    private static final MonetaryAmount MONETARY_AMOUNT_200_EUR = MonetaryAmount.of(AMOUNT_200, CURRENCY_EURO);
    private static final String TRANSACTION_REFERENCE = "Invoice 437263";

    private static final Validator VALIDATOR = TestValidatorConfig.getValidator();

    @ParameterizedTest(name = "[VALID] Should validate successful creation of {0} transaction")
    @EnumSource(TransactionType.class)
    void shouldNotDetectConstraintViolation_whenRequestIsValid(TransactionType transactionType) {
        var monetaryMovement = MonetaryMovement.of(transactionType, MONETARY_AMOUNT_200_EUR);
        var request = new CreateTransactionRequest(BANK_ACCOUNT_ID_JEFFERSON, monetaryMovement, TRANSACTION_REFERENCE);

        request.validate(VALIDATOR);

        assertDoesNotThrow(() -> request.validate(VALIDATOR));
    }

    @ParameterizedTest(name = "[INVALID] Should fail when bankAccountId is null for {0} transaction")
    @EnumSource(TransactionType.class)
    void shouldDetectConstraintViolation_whenBankAccountIdIsNull(TransactionType transactionType) {
        var monetaryMovement = MonetaryMovement.of(transactionType, MONETARY_AMOUNT_200_EUR);
        var request = new CreateTransactionRequest(null, monetaryMovement, TRANSACTION_REFERENCE);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validate(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "transaction.bankAccountId.notNull", "bankAccountId");
    }

    @Test
    void shouldDetectConstraintViolation_whenMonetaryMovementIsNull() {
        var request = new CreateTransactionRequest(BANK_ACCOUNT_ID_JEFFERSON, null, TRANSACTION_REFERENCE);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validate(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "transaction.monetaryMovement.notNull", "monetaryMovement");
    }

    @ParameterizedTest(name = "[INVALID] Should fail when reference exceeds 50 characters for {0} transaction")
    @EnumSource(TransactionType.class)
    void shouldDetectConstraintViolation_whenTransferReferenceExceeds50Characters(TransactionType transactionType) {
        var veryLongTransferReference = "J".repeat(51);
        var monetaryMovement = MonetaryMovement.of(transactionType, MONETARY_AMOUNT_200_EUR);
        var request = new CreateTransactionRequest(BANK_ACCOUNT_ID_JEFFERSON, monetaryMovement, veryLongTransferReference);

        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
                request.validate(VALIDATOR)
        );

        assertSingleViolation(exception.getConstraintViolations(),
                "transaction.reference.tooLong", "reference");

    }
}