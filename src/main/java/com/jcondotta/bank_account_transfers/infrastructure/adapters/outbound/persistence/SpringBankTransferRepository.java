package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence;

import com.jcondotta.bank_account_transfers.domain.BankTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringBankTransferRepository extends JpaRepository<BankTransfer, UUID> {

    Optional<BankTransfer> findByIdempotencyKey(UUID idempotencyKey);
}
