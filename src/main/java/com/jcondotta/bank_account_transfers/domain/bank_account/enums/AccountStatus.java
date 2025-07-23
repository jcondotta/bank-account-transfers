package com.jcondotta.bank_account_transfers.domain.bank_account.enums;

public enum AccountStatus {
    PENDING, ACTIVE, CANCELLED, BLOCKED;

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean isBlocked() {
        return this == BLOCKED;
    }
}