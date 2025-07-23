package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.enums;

public enum TransferStatusEntity {
    PENDING,
    COMPLETED,
    FAILED;

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isFailed() {
        return this == FAILED;
    }
}

