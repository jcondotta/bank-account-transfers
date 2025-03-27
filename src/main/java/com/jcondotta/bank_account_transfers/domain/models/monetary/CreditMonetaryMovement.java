package com.jcondotta.bank_account_transfers.domain.models.monetary;

import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import com.jcondotta.bank_account_transfers.domain.shared.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Represents a credit movement in a bank transfer.")
public record CreditMonetaryMovement(MonetaryAmount monetaryAmount) implements MonetaryMovement {

    public CreditMonetaryMovement {
        Objects.requireNonNull(monetaryAmount, ValidationMessages.MONETARY_AMOUNT_REQUIRED);
    }

    @Override
    public TransactionType transactionType() {
        return TransactionType.CREDIT;
    }
}