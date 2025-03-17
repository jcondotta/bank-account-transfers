package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.domain.FinancialTransaction;
import com.jcondotta.bank_account_transfers.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FinancialTransactionDTO(
        UUID financialTransactionId,
        UUID bankAccountId,
        BigDecimal amount,
        String currency,
        TransactionType transactionType
) {
    public FinancialTransactionDTO(FinancialTransaction financialTransaction) {
        this(
            financialTransaction.getFinancialTransactionId(),
            financialTransaction.getBankAccountId(),
            financialTransaction.getAmount(),
            financialTransaction.getCurrency(),
            financialTransaction.getTransactionType()
        );
    }
}
