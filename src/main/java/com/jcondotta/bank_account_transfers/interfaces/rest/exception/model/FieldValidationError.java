package com.jcondotta.bank_account_transfers.interfaces.rest.exception.model;

import java.util.List;

public record FieldValidationError(String field, List<String> messages) {

    public static FieldValidationError of(String field, List<String> messages) {
        return new FieldValidationError(field, messages);
    }

    public static FieldValidationError of(String field, String message) {
        return new FieldValidationError(field, List.of(message));
    }
}
