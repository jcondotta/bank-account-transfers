package com.jcondotta.bank_account_transfers.domain.models.monetary;

import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import com.jcondotta.bank_account_transfers.domain.shared.ValidationMessages;

import java.math.BigDecimal;
import java.util.Objects;

public sealed interface MonetaryMovement
        extends MonetaryAmount
        permits DebitMonetaryMovement, CreditMonetaryMovement {

    TransactionType transactionType();
    MonetaryAmount monetaryAmount();

    @Override
    default BigDecimal amount(){
        return monetaryAmount().amount();
    }

    @Override
    default String currency(){
        return monetaryAmount().currency();
    }

    static MonetaryMovement of(TransactionType transactionType, MonetaryAmount monetaryAmount) {
        Objects.requireNonNull(transactionType, ValidationMessages.TRANSACTION_TYPE_REQUIRED);
        Objects.requireNonNull(monetaryAmount, ValidationMessages.MONETARY_AMOUNT_REQUIRED);

        return switch (transactionType) {
            case CREDIT -> new CreditMonetaryMovement(monetaryAmount);
            case DEBIT -> new DebitMonetaryMovement(monetaryAmount);
        };
    }

    static MonetaryMovement credit(MonetaryAmount amount) {
        return new CreditMonetaryMovement(amount);
    }

    static MonetaryMovement debit(MonetaryAmount amount) {
        return new DebitMonetaryMovement(amount);
    }
}