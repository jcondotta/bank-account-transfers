package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import java.util.Objects;

public record PartyName(String value) {

    public static final String NAME_NOT_NULL_MESSAGE = "name must not be null.";
    public static final String NAME_NOT_EMPTY_MESSAGE = "name must not be empty.";
    public static final String NAME_TOO_LONG_MESSAGE = "name must not be longer than 255 characters.";

    public PartyName {
        Objects.requireNonNull(value, NAME_NOT_NULL_MESSAGE);

        if(value.trim().isBlank()) {
            throw new IllegalArgumentException(NAME_NOT_EMPTY_MESSAGE);
        }
        if(value.trim().length() > 255) {
            throw new IllegalArgumentException(NAME_TOO_LONG_MESSAGE);
        }

        value = value.trim();
    }

    public static PartyName of(String value) {
        return new PartyName(value);
    }
}
