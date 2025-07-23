package com.jcondotta.bank_account_transfers.domain.shared.valueobjects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTest {

    @Test
    void shouldRecognizeEuro() {
        Currency currency = Currency.EUR;

        assertThat(currency.symbol()).isEqualTo("€");
        assertThat(currency.description()).isEqualTo("Euro");
    }

    @Test
    void shouldRecognizeUSDollar() {
        Currency currency = Currency.USD;

        assertThat(currency.symbol()).isEqualTo("$");
        assertThat(currency.description()).isEqualTo("US Dollar");
    }

    @Test
    void shouldRecognizeBritishPound() {
        Currency currency = Currency.GBP;

        assertThat(currency.symbol()).isEqualTo("£");
        assertThat(currency.description()).isEqualTo("British Pound");
    }
}