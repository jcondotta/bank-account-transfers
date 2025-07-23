package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.entity;

import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.enums.CurrencyEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class MonetaryAmountEmbeddable {

    protected MonetaryAmountEmbeddable() {}

    public MonetaryAmountEmbeddable(BigDecimal amount, CurrencyEntity currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @Column(name = "amount", nullable = false, precision = 38, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 3)
    private CurrencyEntity currency;

    public static MonetaryAmountEmbeddable of(BigDecimal amount, CurrencyEntity currency) {
        return new MonetaryAmountEmbeddable(amount, currency);
    }

    public static MonetaryAmountEmbeddable of(BigDecimal amount, String currency) {
        return new MonetaryAmountEmbeddable(amount, CurrencyEntity.valueOf(currency));
    }
}