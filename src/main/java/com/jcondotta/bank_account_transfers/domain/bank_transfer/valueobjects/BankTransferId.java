package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record BankTransferId(UUID value) {

    public static final String BANK_TRANSFER_ID_NOT_NULL_MESSAGE = "bank transfer id must not be null.";

    public BankTransferId {
        Objects.requireNonNull(value, BANK_TRANSFER_ID_NOT_NULL_MESSAGE);
    }

    public static BankTransferId of(UUID value) {
        return new BankTransferId(value);
    }

    public static BankTransferId newId() {
        return new BankTransferId(UUID.randomUUID());
    }

    public boolean is(UUID other) {
        return value.equals(other);
    }
}
