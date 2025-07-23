package com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects;

import com.jcondotta.bank_account_transfers.domain.monetary_movement.exceptions.InvalidMonetaryAmountException;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record MonetaryAmount(BigDecimal amount, Currency currency) {

    public static final String AMOUNT_NOT_NULL_MESSAGE = "amount must not be null.";
    public static final String CURRENCY_NOT_NULL_MESSAGE = "currency must not be null.";
    public static final String AMOUNT_POSITIVE_MESSAGE = "amount must not be negative.";

    public MonetaryAmount {
        requireNonNull(amount, AMOUNT_NOT_NULL_MESSAGE);
        requireNonNull(currency, CURRENCY_NOT_NULL_MESSAGE);

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw InvalidMonetaryAmountException.negativeAmount();
        }
    }

    public static MonetaryAmount of(BigDecimal amount, Currency currency) {
        return new MonetaryAmount(amount, currency);
    }
}