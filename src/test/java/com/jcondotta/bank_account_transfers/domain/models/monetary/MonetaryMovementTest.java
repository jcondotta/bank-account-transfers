package com.jcondotta.bank_account_transfers.domain.models.monetary;

import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MonetaryMovementTest {

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";

    private static final MonetaryAmount MONETARY_AMOUNT_200_EUR = MonetaryAmount.of(AMOUNT_200, CURRENCY_EURO);

    @ParameterizedTest(name = "[VALID] Should create {0} MonetaryMovement correctly")
    @EnumSource(TransactionType.class)
    void shouldCreateMonetaryMovement_whenParametersAreValid(TransactionType transactionType) {
        var monetaryMovement = MonetaryMovement.of(transactionType, MONETARY_AMOUNT_200_EUR);

        assertAll(
                () -> assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200),
                () -> assertThat(monetaryMovement.currency()).isEqualTo(CURRENCY_EURO),
                () -> assertThat(monetaryMovement.transactionType()).isEqualTo(transactionType)
        );
    }

    @Test
    void shouldCreateMonetaryMovement_whenCreditMonetaryAmountIsValid() {
        var monetaryMovement = MonetaryMovement.credit(MONETARY_AMOUNT_200_EUR);

        assertAll(
                () -> assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200),
                () -> assertThat(monetaryMovement.currency()).isEqualTo(CURRENCY_EURO),
                () -> assertThat(monetaryMovement.transactionType()).isEqualTo(TransactionType.CREDIT)
        );
    }

    @Test
    void shouldCreateMonetaryMovement_whenDebitMonetaryAmountIsValid() {
        var monetaryMovement = MonetaryMovement.debit(MONETARY_AMOUNT_200_EUR);

        assertAll(
                () -> assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200),
                () -> assertThat(monetaryMovement.currency()).isEqualTo(CURRENCY_EURO),
                () -> assertThat(monetaryMovement.transactionType()).isEqualTo(TransactionType.DEBIT)
        );
    }

    @ParameterizedTest
    @EnumSource(TransactionType.class)
    public void shouldThrowNullPointerException_whenMonetaryAmountIsNull(TransactionType transactionType) {
        assertThatThrownBy(() -> MonetaryMovement.of(transactionType, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetaryMovement.monetaryAmount.notNull");
    }

    @Test
    public void shouldThrowNullPointerException_whenTransactionTypeIsNull() {
        assertThatThrownBy(() -> MonetaryMovement.of(null, MONETARY_AMOUNT_200_EUR))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetaryMovement.transactionType.notNull");
    }

    @Test
    public void shouldThrowNullPointerException_whenCreditMonetaryAmountIsNull() {
        assertThatThrownBy(() -> MonetaryMovement.credit(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetaryMovement.monetaryAmount.notNull");
    }

    @Test
    public void shouldThrowNullPointerException_whenDebitMonetaryAmountIsNull() {
        assertThatThrownBy(() -> MonetaryMovement.debit(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetaryMovement.monetaryAmount.notNull");
    }
}