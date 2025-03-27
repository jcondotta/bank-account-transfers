package com.jcondotta.bank_account_transfers.domain.models.monetary;

import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class DebitMonetaryMovementTest {

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";

    private static final MonetaryAmount MONETARY_AMOUNT_200_EUR = MonetaryAmount.of(AMOUNT_200, CURRENCY_EURO);

    @Test
    void shouldCreateDebitMonetaryMovement_whenRequestIsValid() {
        var monetaryMovement = new DebitMonetaryMovement(MONETARY_AMOUNT_200_EUR);

        assertAll(
                () -> assertThat(monetaryMovement.transactionType()).isEqualTo(TransactionType.DEBIT),
                () -> assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200),
                () -> assertThat(monetaryMovement.currency()).isEqualTo(CURRENCY_EURO)
        );
    }

    @Test
    void shouldCreateDebitMonetaryMovement_whenMonetaryAmountIsNull() {
        assertThatThrownBy(() -> new DebitMonetaryMovement(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("monetaryMovement.monetaryAmount.notNull");
    }
}