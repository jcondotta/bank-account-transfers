package com.jcondotta.bank_account_transfers.domain.shared.exceptions;

import java.util.Map;

public abstract class BusinessRuleException extends RuntimeException {

    protected BusinessRuleException(String message) {
        super(message);
    }

    public abstract String getType();

    public Map<String, Object> getProperties() {
        return Map.of();
    }
}