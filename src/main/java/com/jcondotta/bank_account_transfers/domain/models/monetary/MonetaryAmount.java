package com.jcondotta.bank_account_transfers.domain.models.monetary;

import java.math.BigDecimal;
import java.util.Objects;

import static com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryAmount.MonetaryAmountImpl;

public sealed interface MonetaryAmount permits MonetaryAmountImpl, MonetaryMovement {

    BigDecimal amount();
    String currency();

    static MonetaryAmount of(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "monetary.amount.notNull");
        Objects.requireNonNull(currency, "monetary.currency.notNull");

        return new MonetaryAmountImpl(amount, currency);
    }

    final class MonetaryAmountImpl implements MonetaryAmount {
        private final BigDecimal amount;
        private final String currency;

        private MonetaryAmountImpl(BigDecimal amount, String currency) {
            this.amount = amount;
            this.currency = currency;
        }

        @Override
        public BigDecimal amount() {
            return amount;
        }
        @Override
        public String currency() {
            return currency;
        }
    }
}
