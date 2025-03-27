package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction;

import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Represents the details of a financial transaction within a bank transfer operation.")
public record TransactionDTO(

        @Schema(description = "Unique identifier of the financial transaction.", requiredMode = RequiredMode.REQUIRED, example = "f9eb3a6c-1cc6-4b8b-a2b8-4f6abc5e9749")
        UUID transactionId,

        @Schema(description = "UUID of the bank account related to this transaction.", requiredMode = RequiredMode.REQUIRED, example = "cfe86bfa-a8d9-4f02-9b83-7c23da3316ae")
        UUID bankAccountId,

        @Schema(description = "Type of the transaction: DEBIT or CREDIT.", requiredMode = RequiredMode.REQUIRED, example = "DEBIT")
        TransactionType transactionType,

        @Schema(description = "Transaction amount in the specified currency.", requiredMode = RequiredMode.REQUIRED, example = "250.17")
        BigDecimal amount,

        @Schema(description = "The ISO 4217 currency code for the transaction.", requiredMode = RequiredMode.REQUIRED, example = "EUR")
        String currency,

        @Schema(description = "Reference or description associated with the transaction.", example = "Invoice payment 456789")
        String reference,

        @Schema(description = "Timestamp indicating when the transaction was created.", requiredMode = RequiredMode.REQUIRED, example = "2025-03-24T18:00:00Z")
        Instant createdAt

) { }
