package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.domain.BankTransfer;

import java.time.Instant;
import java.util.UUID;

public record BankTransferDTO(UUID bankTransferId, String reference, Instant createdAt) {

    public BankTransferDTO(BankTransfer bankTransfer) {
        this(bankTransfer.getBankTransferId(), bankTransfer.getReference(), bankTransfer.getCreatedAt());
    }
}
