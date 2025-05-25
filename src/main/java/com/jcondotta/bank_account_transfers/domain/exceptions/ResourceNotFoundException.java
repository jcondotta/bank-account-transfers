package com.jcondotta.bank_account_transfers.domain.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private final String field;
    private final String value;

    public ResourceNotFoundException(String message, String field, String value) {
        super(message);
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}