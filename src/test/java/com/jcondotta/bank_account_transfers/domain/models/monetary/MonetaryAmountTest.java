package com.jcondotta.bank_account_transfers.domain.models.monetary;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MonetaryAmountTest {

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";

    @Test
    public void shouldCreateMonetaryAmount_whenParametersAreValid() {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, CURRENCY_EURO);

        assertAll(
                () -> assertThat(monetaryAmount.amount()).isEqualTo(AMOUNT_200),
                () -> assertThat(monetaryAmount.currency()).isEqualTo(CURRENCY_EURO)
        );
    }

    @Test
    public void shouldThrowNullPointerException_whenAmountIsNull() {
        assertThatThrownBy(() -> MonetaryAmount.of(null, CURRENCY_EURO))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetary.amount.notNull");
    }

    @Test
    public void shouldThrowNullPointerException_whenCurrencyIsNull() {
        assertThatThrownBy(() -> MonetaryAmount.of(AMOUNT_200, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetary.currency.notNull");
    }
}