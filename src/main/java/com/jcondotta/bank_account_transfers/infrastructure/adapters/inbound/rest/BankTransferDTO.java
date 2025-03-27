package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Represents the details of a created Bank Transfer")
public record BankTransferDTO(

        @Schema(
                description = "Unique identifier of the bank transfer",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID bankTransferId

//        @Schema(
//                description = "Reference or description for the bank transfer",
//                example = "Invoice 456789"
//        )
//        String reference,
//
//        @Schema(
//                description = "Timestamp when the bank transfer was created",
//                example = "2025-03-18T11:41:13.966Z"
//        )
//        Instant createdAt,
//
//        @Schema(
//                description = "List of financial transactions (debit/credit) associated with this bank transfer"
//        )
//        List<TransactionDTO> financialTransactions
) {

//    public BankTransferDTO(BankTransfer bankTransfer) {
//        this(
//                bankTransfer.getBankTransferId(),
//                bankTransfer.getReference(),
//                bankTransfer.getCreatedAt(),
//                bankTransfer.getFinancialTransactions()
//                        .stream()
//                        .map(TransactionDTO::new)
//                        .collect(Collectors.toList())
//        );
//    }
}
