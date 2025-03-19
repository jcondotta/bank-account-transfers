package com.jcondotta.bank_account_transfers.application.ports.inbound;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestValidatorConfig;
import jakarta.validation.Validator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ValidationPortTest {

    private static final UUID IDEMPOTENCY_KEY = UUID.fromString("8d4c6f5c-3726-4b44-94a7-29a016853fb1");

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();

    private static final String RECIPIENT_NAME_PATRIZIO = TestBankAccount.PATRIZIO.getAccountHolderName();
    private static final String RECIPIENT_IBAN_PATRIZIO = TestBankAccount.PATRIZIO.getIban();

    private static final BigDecimal TRANSFER_AMOUNT_TWO_HUNDRED = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";
    private static final String TRANSFER_REFERENCE = "INVOICE 877635";

    private static final Validator VALIDATOR = TestValidatorConfig.getValidator();


//    @Test
//    void shouldSuccess_whenRequestIsValid(){
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        bankTransferValidator.validate(IDEMPOTENCY_KEY, request);
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenIdempotencyKeyIsNull(){
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> bankTransferValidator.validate(null, request))
//                .isInstanceOf(NullPointerException.class)
//                .hasMessage("idempotencyKey.notNull");
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenSenderBankAccountIdIsNull() {
//        var request = new CreateBankTransferRequest(
//                null,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> bankTransferValidator.validate(IDEMPOTENCY_KEY, request))
//                .isInstanceOf(ConstraintViolationException.class)
//                .extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
//                .satisfies(ex -> assertSingleViolation(
//                        ex, "transfer.senderBankAccountId.notNull", "senderBankAccountId"));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldThrowConstraintViolationException_whenRecipientIbanIsBlank(String recipientIban) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                recipientIban,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> bankTransferValidator.validate(IDEMPOTENCY_KEY, request))
//                .isInstanceOf(ConstraintViolationException.class)
//                .extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
//                .satisfies(ex -> assertSingleViolation(
//                        ex, "transfer.recipientIban.notBlank", "recipientIban"));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldThrowConstraintViolationException_whenSenderNameIsBlank(String recipientName) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                recipientName,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> bankTransferValidator.validate(IDEMPOTENCY_KEY, request))
//                .isInstanceOf(ConstraintViolationException.class)
//                .extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
//                .satisfies(ex -> assertSingleViolation(
//                        ex,  "transfer.recipientName.notBlank", "recipientName"));
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenAmountIsNull() {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                null,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> bankTransferValidator.validate(IDEMPOTENCY_KEY, request))
//                .isInstanceOf(ConstraintViolationException.class)
//                .extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
//                .satisfies(ex -> assertSingleViolation(
//                        ex,  "transfer.amount.notNull", "amount"));
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"-10.00", "0.00"})
//    void shouldThrowConstraintViolationException_whenAmountIsNotPositive(String invalidAmount) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                new BigDecimal(invalidAmount),
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> bankTransferValidator.validate(IDEMPOTENCY_KEY, request))
//                .isInstanceOf(ConstraintViolationException.class)
//                .extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
//                .satisfies(ex -> assertSingleViolation(
//                        ex,  "transfer.amount.positive", "amount"));
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldThrowConstraintViolationException_whenCurrencyIsBlank(String invalidCurrency) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                invalidCurrency,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> bankTransferValidator.validate(IDEMPOTENCY_KEY, request))
//                .isInstanceOf(ConstraintViolationException.class)
//                .extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
//                .satisfies(ex -> assertSingleViolation(
//                        ex,  "transfer.currency.notBlank", "currency"));
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenTransferReferenceExceeds50Characters() {
//        var veryLongTransferReference = "J".repeat(51);
//
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                veryLongTransferReference
//        );
//
//        assertThatThrownBy(() -> bankTransferValidator.validate(IDEMPOTENCY_KEY, request))
//                .isInstanceOf(ConstraintViolationException.class)
//                .extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
//                .satisfies(ex -> assertSingleViolation(
//                        ex,  "transfer.reference.tooLong", "reference"));
//    }
//
//    private void assertSingleViolation(Set<ConstraintViolation<?>> constraintViolations, String expectedMessage, String expectedProperty) {
//        assertThat(constraintViolations)
//                .singleElement()
//                .extracting(ConstraintViolation::getMessage, v -> v.getPropertyPath().toString())
//                .containsExactly(expectedMessage, expectedProperty);
//    }
}