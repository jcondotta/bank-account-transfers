package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MonetaryAmountsTest {

    private static final BigDecimal TRANSFER_AMOUNT_TWO_HUNDRED = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";

    @Test
    public void shouldCreateMonetaryAmount_whenParametersAreValid() {
        MonetaryAmount monetaryAmount = MonetaryAmounts.of(TRANSFER_AMOUNT_TWO_HUNDRED, CURRENCY_EURO);

        assertAll(
                () -> assertThat(monetaryAmount.amount()).isEqualTo(TRANSFER_AMOUNT_TWO_HUNDRED),
                () -> assertThat(monetaryAmount.currency()).isEqualTo(CURRENCY_EURO)
        );
    }

    @Test
    public void shouldThrowNullPointerException_whenAmountIsNull() {
        assertThatThrownBy(() -> MonetaryAmounts.of(null, CURRENCY_EURO))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetary.amount.notNull");
    }

    @Test
    public void shouldThrowNullPointerException_whenCurrencyIsNull() {
        assertThatThrownBy(() -> MonetaryAmounts.of(TRANSFER_AMOUNT_TWO_HUNDRED, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetary.currency.notNull");
    }
}