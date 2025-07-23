package com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects;

import com.jcondotta.bank_account_transfers.argument_provider.MovementTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.enums.MovementType;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonetaryMovementTest {

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");

    @ParameterizedTest
    @ArgumentsSource(MovementTypeAndCurrencyArgumentsProvider.class)
    void shouldCreateMonetaryMovement_whenRequestIsValid(MovementType movementType, Currency currency) {
        var currentMonetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var currentMonetaryMovement = MonetaryMovement.of(movementType, currentMonetaryAmount);

        assertThat(currentMonetaryMovement)
            .satisfies(monetaryMovement -> {
                    assertThat(monetaryMovement.movementType()).isEqualTo(movementType);
                    assertThat(monetaryMovement.isDebit()).isEqualTo(movementType.isDebit());
                    assertThat(monetaryMovement.isCredit()).isEqualTo(movementType.isCredit());
                    assertThat(monetaryMovement.currency()).isEqualTo(currency);
                    assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200);

                    assertThat(monetaryMovement.monetaryAmount())
                        .satisfies(monetaryAmount -> {
                            assertThat(monetaryMovement.currency()).isEqualTo(currency);
                            assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200);
                        }
                    );
                }
            );
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateDebitMonetaryMovement_whenUsingOfFactoryMethod(Currency currency) {
        var currentMonetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var debitMonetaryMovement = MonetaryMovement.ofDebit(currentMonetaryAmount);

        assertThat(debitMonetaryMovement)
            .satisfies(monetaryMovement -> {
                    assertThat(monetaryMovement.movementType()).isEqualTo(MovementType.DEBIT);
                    assertThat(monetaryMovement.isDebit()).isTrue();
                    assertThat(monetaryMovement.isCredit()).isFalse();
                    assertThat(monetaryMovement.currency()).isEqualTo(currency);
                    assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200);

                    assertThat(monetaryMovement.monetaryAmount())
                        .satisfies(monetaryAmount -> {
                                assertThat(monetaryMovement.currency()).isEqualTo(currency);
                                assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200);
                            }
                        );
                }
            );
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateCreditMonetaryMovement_whenUsingOfFactoryMethod(Currency currency) {
        var currentMonetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var creditMonetaryMovement = MonetaryMovement.ofCredit(currentMonetaryAmount);

        assertThat(creditMonetaryMovement)
            .satisfies(monetaryMovement -> {
                    assertThat(monetaryMovement.movementType()).isEqualTo(MovementType.CREDIT);
                    assertThat(monetaryMovement.isCredit()).isTrue();
                    assertThat(monetaryMovement.isDebit()).isFalse();
                    assertThat(monetaryMovement.currency()).isEqualTo(currency);
                    assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200);

                    assertThat(monetaryMovement.monetaryAmount())
                        .satisfies(monetaryAmount -> {
                                assertThat(monetaryMovement.currency()).isEqualTo(currency);
                                assertThat(monetaryMovement.amount()).isEqualTo(AMOUNT_200);
                            }
                        );
                }
            );
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenMovementTypeIsNull(Currency currency) {
        var currentMonetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        assertThatThrownBy(() -> MonetaryMovement.of(null, currentMonetaryAmount))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(MonetaryMovement.MOVEMENT_TYPE_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @EnumSource(MovementType.class)
    void shouldThrowNullPointerException_whenMovementTypeIsNull(MovementType movementType) {
        assertThatThrownBy(() -> MonetaryMovement.of(movementType, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(MonetaryMovement.MONETARY_AMOUNT_NOT_NULL_MESSAGE);
    }
}