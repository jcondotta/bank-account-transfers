package com.jcondotta.bank_account_transfers.application.ports.outbound.persistence;

import com.jcondotta.bank_account_transfers.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepositoryPort extends JpaRepository<Transaction, UUID> {
//    Transaction save(Transaction transaction);
//
//    Transaction saveAll(Transaction transaction);
//    void saveAll(Transaction... transaction);


}