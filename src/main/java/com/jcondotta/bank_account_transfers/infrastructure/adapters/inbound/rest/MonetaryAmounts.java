package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import java.math.BigDecimal;
import java.util.Objects;

public final class MonetaryAmounts {

    private MonetaryAmounts() {}

    public static MonetaryAmount of(BigDecimal amount, String currency) {
        return new MonetaryAmount() {
            private final BigDecimal validAmount = Objects.requireNonNull(amount, "monetary.amount.notNull");
            private final String validCurrency = Objects.requireNonNull(currency, "monetary.currency.notNull");

            @Override
            public BigDecimal amount() {
                return validAmount;
            }

            @Override
            public String currency() {
                return validCurrency;
            }
        };
    }
}
