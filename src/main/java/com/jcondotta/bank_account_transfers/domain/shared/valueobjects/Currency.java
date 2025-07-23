package com.jcondotta.bank_account_transfers.domain.shared.valueobjects;

public enum Currency {
    EUR("Euro", "€"),
    USD("US Dollar", "$"),
    GBP("British Pound", "£");

    private final String description;
    private final String symbol;

    Currency(String description, String symbol) {
        this.description = description;
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }

    public String description() {
        return description;
    }
}
