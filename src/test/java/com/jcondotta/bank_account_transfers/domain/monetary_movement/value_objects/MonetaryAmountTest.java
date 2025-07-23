package com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects;

import com.jcondotta.bank_account_transfers.domain.monetary_movement.exceptions.InvalidMonetaryAmountException;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MonetaryAmountTest {

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");

    @ParameterizedTest
    @EnumSource(Currency.class)
    public void shouldCreateMonetaryAmount_whenParametersAreValid(Currency currency) {
        assertThat(MonetaryAmount.of(AMOUNT_200, currency))
            .satisfies(monetaryAmount ->
                assertAll(
                    () -> assertThat(monetaryAmount.amount()).isEqualTo(AMOUNT_200),
                    () -> assertThat(monetaryAmount.currency()).isEqualTo(currency)
                )
            );
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    public void shouldCreateMonetaryAmount_whenAmountIsZero(Currency currency) {
        assertThat(MonetaryAmount.of(BigDecimal.ZERO, currency))
            .satisfies(monetaryAmount ->
                assertAll(
                    () -> assertThat(monetaryAmount.amount()).isEqualTo(BigDecimal.ZERO),
                    () -> assertThat(monetaryAmount.currency()).isEqualTo(currency)
                )
            );
    }

    @ParameterizedTest
    @CsvSource({"-0.01", "-1.00", "-100.00"})
    public void shouldThrowInvalidMonetaryAmountException_whenAmountIsNegative(String amount) {
        assertThatThrownBy(() -> MonetaryAmount.of(new BigDecimal(amount), Currency.USD))
            .isInstanceOf(InvalidMonetaryAmountException.class)
            .hasMessage(MonetaryAmount.AMOUNT_POSITIVE_MESSAGE);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    public void shouldThrowNullPointerException_whenAmountIsNull(Currency currency) {
        assertThatThrownBy(() -> MonetaryAmount.of(null, currency))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(MonetaryAmount.AMOUNT_NOT_NULL_MESSAGE);
    }

    @Test
    public void shouldThrowNullPointerException_whenCurrencyIsNull() {
        assertThatThrownBy(() -> MonetaryAmount.of(AMOUNT_200, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(MonetaryAmount.CURRENCY_NOT_NULL_MESSAGE);
    }
}