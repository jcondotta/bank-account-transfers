package com.jcondotta.bank_account_transfers.application.ports.outbound.persistence;

import com.jcondotta.bank_account_transfers.domain.BankTransfer;

import java.util.Optional;
import java.util.UUID;

public interface BankTransferRepositoryPort {
    BankTransfer save(BankTransfer bankTransfer);

    Optional<BankTransfer> findByIdempotencyKey(UUID idempotencyKey);
//
//    Optional<BankTransfer> findById(UUID bankTransferId);
//
//    List<BankTransfer> findAll();
}