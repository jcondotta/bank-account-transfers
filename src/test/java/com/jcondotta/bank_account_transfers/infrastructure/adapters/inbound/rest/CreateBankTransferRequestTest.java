package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestValidatorConfig;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class CreateBankTransferRequestTest {

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();
    private static final String RECIPIENT_NAME_PATRIZIO = TestBankAccount.PATRIZIO.getAccountHolderName();
    private static final String RECIPIENT_IBAN_PATRIZIO = TestBankAccount.PATRIZIO.getIban();

    private static final BigDecimal TRANSFER_AMOUNT_TWO_HUNDRED = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";
    private static final String TRANSFER_REFERENCE = "Playstation 4";

    private static final Validator VALIDATOR = TestValidatorConfig.getValidator();

    @Test
    void shouldNotDetectConstraintViolation_whenRequestIsValid() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void shouldDetectConstraintViolation_whenSenderBankAccountIdIsNull() {
        var request = new CreateBankTransferRequest(
                null,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.senderBankAccountId.notNull", "senderBankAccountId");
    }

    @Test
    void shouldDetectConstraintViolation_whenRecipientIbanIsNull() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                null,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.recipientIban.notNull", "recipientIban");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ES12", "GB1", "DE123"})
    void shouldDetectConstraintViolation_whenRecipientIbanIsShorterThan15Characters(String tooShortIban) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                tooShortIban,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.recipientIban.size", "recipientIban");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ES12345678901234567890123456789012345", "FR763000400003123456789018762345678901"})
    void shouldDetectConstraintViolation_whenRecipientIbanExceeds34Characters(String tooLongIban) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                tooLongIban,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.recipientIban.size", "recipientIban");
    }

    @Test
    void shouldDetectConstraintViolation_whenRecipientNameIsNull() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                null,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.recipientName.notNull", "recipientName");
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "Ab", "J.C", "Jo"})
    void shouldDetectConstraintViolation_whenRecipientNameIsShorterThan5Characters(String shortRecipientName) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                shortRecipientName,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.recipientName.size", "recipientName");
    }

    @Test
    void shouldDetectConstraintViolation_whenRecipientNameExceeds100Characters() {
        var veryLongRecipientName = "J".repeat(101);

        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                veryLongRecipientName,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.recipientName.size", "recipientName");
    }

    @Test
    void shouldDetectConstraintViolation_whenAmountIsNull() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                null,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.amount.notNull", "amount");
    }

    @ParameterizedTest
    @ValueSource(strings = {"-10.00", "0.00"})
    void shouldDetectConstraintViolation_whenAmountIsNotPositive(String invalidAmount) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                new BigDecimal(invalidAmount),
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.amount.positive", "amount");
    }

    @Test
    void shouldDetectConstraintViolation_whenAmountIsTooLarge() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                new BigDecimal("1000000000000.00"),
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.amount.invalidPrecision", "amount");
    }

    @Test
    void shouldDetectConstraintViolation_whenAmountHasTooManyDecimalPlaces() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                new BigDecimal("100.123"),
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.amount.invalidPrecision", "amount");
    }

    @Test
    void shouldDetectMultipleViolations_whenAmountIsNegativeAndExceedsAllowedPrecision() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                new BigDecimal("-20.300"),
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);

        assertThat(constraintViolations)
                .extracting(v -> tuple(v.getMessage(), v.getPropertyPath().toString()))
                .containsExactlyInAnyOrder(
                        tuple("transfer.amount.positive", "amount"),
                        tuple("transfer.amount.invalidPrecision", "amount")
                );
    }

    @Test
    void shouldDetectConstraintViolation_whenCurrencyIsNull() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                null,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.currency.notNull", "currency");
    }

    @ParameterizedTest
    @ValueSource(strings = {"usD", "eu1", "1EU", "US9", "123", "USDX", "EURO"})
    void shouldDetectConstraintViolation_whenCurrencyHasInvalidFormat(String invalidCurrency) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                invalidCurrency,
                TRANSFER_REFERENCE
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.currency.invalidFormat", "currency");
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldNotDetectConstraintViolation_whenReferenceIsBlank(String transferReference) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                transferReference
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    void shouldDetectConstraintViolation_whenTransferReferenceExceeds50Characters() {
        var veryLongTransferReference = "J".repeat(51);

        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                RECIPIENT_NAME_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                veryLongTransferReference
        );

        var constraintViolations = VALIDATOR.validate(request);
        assertSingleViolation(constraintViolations, "transfer.reference.tooLong", "reference");
    }

    private void assertSingleViolation(Set<ConstraintViolation<CreateBankTransferRequest>> constraintViolations, String expectedMessage, String expectedProperty) {
        assertThat(constraintViolations)
                .singleElement()
                .extracting(ConstraintViolation::getMessage, v -> v.getPropertyPath().toString())
                .containsExactly(expectedMessage, expectedProperty);
    }
}
