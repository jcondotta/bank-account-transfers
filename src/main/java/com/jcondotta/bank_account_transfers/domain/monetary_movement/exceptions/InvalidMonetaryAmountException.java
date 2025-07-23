package com.jcondotta.bank_account_transfers.domain.monetary_movement.exceptions;

import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;

public class InvalidMonetaryAmountException extends RuntimeException {

    public InvalidMonetaryAmountException(String message) {
        super(message);
    }

    public static InvalidMonetaryAmountException negativeAmount() {
        return new InvalidMonetaryAmountException(MonetaryAmount.AMOUNT_POSITIVE_MESSAGE);
    }
}
