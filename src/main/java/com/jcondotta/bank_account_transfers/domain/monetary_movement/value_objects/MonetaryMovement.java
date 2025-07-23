package com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects;

import com.jcondotta.bank_account_transfers.domain.monetary_movement.enums.MovementType;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record MonetaryMovement(MovementType movementType, MonetaryAmount monetaryAmount) {

    public static final String MONETARY_AMOUNT_NOT_NULL_MESSAGE = "monetary amount must not be null.";
    public static final String MOVEMENT_TYPE_NOT_NULL_MESSAGE = "movement type must not be null.";

    public MonetaryMovement {
        requireNonNull(movementType, MOVEMENT_TYPE_NOT_NULL_MESSAGE);
        requireNonNull(monetaryAmount, MONETARY_AMOUNT_NOT_NULL_MESSAGE);
    }

    public static MonetaryMovement of(MovementType movementType, MonetaryAmount monetaryAmount) {
        return new MonetaryMovement(movementType, monetaryAmount);
    }

    public static MonetaryMovement ofDebit(MonetaryAmount monetaryAmount) {
        return new MonetaryMovement(MovementType.DEBIT, monetaryAmount);
    }

    public static MonetaryMovement ofCredit(MonetaryAmount monetaryAmount) {
        return new MonetaryMovement(MovementType.CREDIT, monetaryAmount);
    }

    public BigDecimal amount() {
        return monetaryAmount.amount();
    }

    public Currency currency() {
        return monetaryAmount.currency();
    }

    public boolean isDebit() {
        return movementType.isDebit();
    }

    public boolean isCredit() {
        return movementType.isCredit();
    }
}