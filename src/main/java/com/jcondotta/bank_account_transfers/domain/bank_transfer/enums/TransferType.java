package com.jcondotta.bank_account_transfers.domain.bank_transfer.enums;

public enum TransferType {
    INTERNAL, INCOMING_EXTERNAL, OUTGOING_EXTERNAL;

    public boolean isInternal() {
        return this == INTERNAL;
    }
}