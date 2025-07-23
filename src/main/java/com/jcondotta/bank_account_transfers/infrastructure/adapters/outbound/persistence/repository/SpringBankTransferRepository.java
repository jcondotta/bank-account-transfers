package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.repository;

import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.entity.BankTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringBankTransferRepository extends JpaRepository<BankTransferEntity, UUID> {

}
