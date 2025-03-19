package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.domain.FinancialTransaction;
import com.jcondotta.bank_account_transfers.domain.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Represents the details of a financial transaction within a bank transfer operation.")
public record FinancialTransactionDTO(

        @Schema(
                description = "Unique identifier of the financial transaction.",
                example = "f9eb3a6c-1cc6-4b8b-a2b8-4f6abc5e9749"
        )
        UUID financialTransactionId,

        @Schema(
                description = "UUID of the bank account related to this transaction.",
                example = "cfe86bfa-a8d9-4f02-9b83-7c23da3316ae"
        )
        UUID bankAccountId,

        @Schema(
                description = "Transaction amount in the specified currency.",
                example = "250.17"
        )
        BigDecimal amount,

        @Schema(
                description = "The ISO 4217 currency code for the transaction.",
                example = "EUR"
        )
        String currency,

        @Schema(
                description = "Type of the transaction: DEBIT or CREDIT.",
                example = "DEBIT"
        )
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