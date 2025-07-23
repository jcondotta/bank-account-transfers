package com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_bank_transfer.model;

import com.jcondotta.bank_account_transfers.ValidatorTestFactory;
import com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer.model.CreateInternalBankTransferRestRequest;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CreateOutgoingExternalTransferRestRequestTest {

    private static final UUID SENDER_ACCOUNT_ID = UUID.randomUUID();
    private static final UUID RECIPIENT_ACCOUNT_ID = UUID.randomUUID();

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";

    private static final String TRANSACTION_REFERENCE = "Invoice 437263";

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @Test
    void shouldNotDetectConstraintViolation_whenRequestIsValid() {
        var request = new CreateInternalBankTransferRestRequest(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID.toString(), AMOUNT_200, CURRENCY_EURO, TRANSACTION_REFERENCE);

        assertDoesNotThrow(() -> VALIDATOR.validate(request));
    }

//    @Test
//    void shouldDetectConstraintViolation_whenBankAccountIdIsNull() {
//        var request = new CreateInternalBankTransferRestRequest(null, RECIPIENT_ACCOUNT_ID, AMOUNT_200, CURRENCY_EURO, TRANSACTION_REFERENCE);
//
//        assertThatThrownBy(() -> VALIDATOR.validate(request))
//            .isInstanceOf(ConstraintViolationException.class)
//            .satisfies(constraintViolationException -> {
//                assertThat(constraintViolationException.getMessage().equals(DomainErrorsMessages.BankTransfer.SENDER_ACCOUNT_ID_NOT_NULL));
//            });
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenMonetaryMovementIsNull() {
//        var request = new CreateTransactionRequest(SENDER_BANK_ACCOUNT_ID, null, TRANSACTION_REFERENCE);
//
//        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
//                request.validate(VALIDATOR)
//        );
//
//        assertSingleViolation(exception.getConstraintViolations(),
//                "transaction.monetaryMovement.notNull", "monetaryMovement");
//    }
//
//    @ParameterizedTest(name = "[INVALID] Should fail when reference exceeds 50 characters for {0} transaction")
//    @EnumSource(TransactionType.class)
//    void shouldDetectConstraintViolation_whenTransferReferenceExceeds50Characters(TransactionType transactionType) {
//        var veryLongTransferReference = "J".repeat(51);
//        var monetaryMovement = MonetaryMovement.of(transactionType, MONETARY_AMOUNT_200_EUR);
//        var request = new CreateTransactionRequest(SENDER_BANK_ACCOUNT_ID, monetaryMovement, veryLongTransferReference);
//
//        var exception = assertThrowsExactly(ConstraintViolationException.class, () ->
//                request.validate(VALIDATOR)
//        );
//
//        assertSingleViolation(exception.getConstraintViolations(),
//                "transaction.reference.tooLong", "reference");
//
//    }

}