package com.jcondotta.bank_account_transfers.domain.models.monetary;

import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import com.jcondotta.bank_account_transfers.domain.shared.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Represents a debit movement in a bank transfer.")
public record DebitMonetaryMovement(MonetaryAmount monetaryAmount) implements MonetaryMovement {

    public DebitMonetaryMovement {
        Objects.requireNonNull(monetaryAmount, ValidationMessages.MONETARY_AMOUNT_REQUIRED);
    }

    @Override
    public TransactionType transactionType() {
        return TransactionType.DEBIT;
    }
}