package com.jcondotta.bank_account_transfers.domain.bank_account.enums;

public enum AccountType {
    SAVINGS, CHECKING;

    public boolean isSavings() {
        return this == SAVINGS;
    }

    public boolean isChecking() {
        return this == CHECKING;
    }
}
