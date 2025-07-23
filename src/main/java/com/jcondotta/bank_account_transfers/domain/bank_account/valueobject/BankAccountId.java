package com.jcondotta.bank_account_transfers.domain.bank_account.valueobject;

import java.util.Objects;
import java.util.UUID;

public record BankAccountId(UUID value) {

    public static final String ID_NOT_NULL_MESSAGE = "bankAccountId value must not be null.";

    public BankAccountId {
        Objects.requireNonNull(value, ID_NOT_NULL_MESSAGE);
    }

    public static BankAccountId of(UUID value) {
        return new BankAccountId(value);
    }

    public static BankAccountId newId() {
        return new BankAccountId(UUID.randomUUID());
    }

    public boolean is(UUID other) {
        return value.equals(other);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
