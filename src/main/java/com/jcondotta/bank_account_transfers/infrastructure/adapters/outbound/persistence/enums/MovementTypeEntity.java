package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.enums;

public enum MovementTypeEntity {
    DEBIT,
    CREDIT;

    public boolean isDebit() {
        return this == DEBIT;
    }

    public boolean isCredit() {
        return this == CREDIT;
    }
}
