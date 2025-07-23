package com.jcondotta.bank_account_transfers.domain.bank_transfer.exceptions;

import com.jcondotta.bank_account_transfers.domain.monetary_movement.enums.MovementType;

public class OutgoingTransferMustBeDebitException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = 
        "Outgoing external transfer must be a DEBIT movement, but was: %s";

    public OutgoingTransferMustBeDebitException(MovementType actualMovementType) {
        super(MESSAGE_TEMPLATE.formatted(actualMovementType));
    }
}
