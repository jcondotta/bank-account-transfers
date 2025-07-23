package com.jcondotta.bank_account_transfers.domain.bank_transfer.exceptions;

import com.jcondotta.bank_account_transfers.domain.monetary_movement.enums.MovementType;

public class IncomingTransferMustBeCreditException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE =
        "Incoming external transfer must be a CREDIT movement, but was: %s";

    public IncomingTransferMustBeCreditException(MovementType actualMovementType) {
        super(MESSAGE_TEMPLATE.formatted(actualMovementType));
    }
}
