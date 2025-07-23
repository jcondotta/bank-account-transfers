package com.jcondotta.bank_account_transfers.interfaces.rest.request_internal_transfer.model;

import com.jcondotta.bank_account_transfers.ValidatorTestFactory;
import com.jcondotta.bank_account_transfers.application.TestAccountDetails;
import com.jcondotta.bank_account_transfers.argument_provider.BlankValuesArgumentProvider;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InternalBankTransferRestRequestTest {

    private static final BankAccountId SENDER_ACCOUNT_ID = BankAccountId.newId();
    private static final Iban RECIPIENT_IBAN = Iban.of(TestAccountDetails.JEFFERSON.getIban());

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");

    private static final String TRANSFER_REFERENCE = "Invoice 437263";

    private static final Validator VALIDATOR = ValidatorTestFactory.getValidator();

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldNotDetectViolation_whenRequestIsValid(Currency currency) {
        var request = new InternalBankTransferRestRequest(SENDER_ACCOUNT_ID.value(), RECIPIENT_IBAN.value(), AMOUNT_200, currency.name(), TRANSFER_REFERENCE);
        assertDoesNotThrow(() -> VALIDATOR.validate(request));
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldDetectConstraintViolation_whenBankAccountIdIsNull(Currency currency) {
        var request = new InternalBankTransferRestRequest(null, RECIPIENT_IBAN.value(), AMOUNT_200, currency.name(), TRANSFER_REFERENCE);

        assertThat(VALIDATOR.validate(request))
            .hasSize(1)
            .first()
            .satisfies(violation -> {
                assertThat(violation.getPropertyPath()).hasToString("senderAccountId");
            });
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldDetectConstraintViolation_whenRecipientIbanIsNull(Currency currency) {
        var request = new InternalBankTransferRestRequest(SENDER_ACCOUNT_ID.value(), null, AMOUNT_200, currency.name(), TRANSFER_REFERENCE);

        assertThat(VALIDATOR.validate(request))
            .hasSize(1)
            .first()
            .satisfies(violation -> {
                assertThat(violation.getPropertyPath()).hasToString("recipientIban");
            });
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldDetectConstraintViolation_whenAmountIsNull(Currency currency) {
        var request = new InternalBankTransferRestRequest(SENDER_ACCOUNT_ID.value(), RECIPIENT_IBAN.value(), null, currency.name(), TRANSFER_REFERENCE);

        assertThat(VALIDATOR.validate(request))
            .hasSize(1)
            .first()
            .satisfies(violation -> {
                assertThat(violation.getPropertyPath()).hasToString("amount");
            });
    }

    @Test
    void shouldDetectConstraintViolation_whenCurrencyIsNull() {
        var request = new InternalBankTransferRestRequest(SENDER_ACCOUNT_ID.value(), RECIPIENT_IBAN.value(), AMOUNT_200, null, TRANSFER_REFERENCE);

        assertThat(VALIDATOR.validate(request))
            .hasSize(1)
            .first()
            .satisfies(violation -> {
                assertThat(violation.getPropertyPath()).hasToString("currency");
            });
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldNotDetectConstraintViolation_whenReferenceIsNull(Currency currency) {
        var request = new InternalBankTransferRestRequest(SENDER_ACCOUNT_ID.value(), RECIPIENT_IBAN.value(), AMOUNT_200, currency.name(), null);

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }

    @ParameterizedTest
    @ArgumentsSource(BlankValuesArgumentProvider.class)
    void shouldNotDetectConstraintViolation_whenReferenceIsNull(String blankReference) {
        var request = new InternalBankTransferRestRequest(SENDER_ACCOUNT_ID.value(), RECIPIENT_IBAN.value(), AMOUNT_200, Currency.GBP.name(), blankReference);

        assertThat(VALIDATOR.validate(request)).isEmpty();
    }
}