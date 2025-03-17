package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence;

import com.jcondotta.bank_account_transfers.application.ports.outbound.persistence.BankTransferRepositoryPort;
import com.jcondotta.bank_account_transfers.domain.BankTransfer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class JpaBankTransferRepositoryAdapter implements BankTransferRepositoryPort {

    private final SpringBankTransferRepository repository;

    public JpaBankTransferRepositoryAdapter(SpringBankTransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<BankTransfer> findByIdempotencyKey(UUID idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey);
    }

    public BankTransfer save(BankTransfer bankTransfer) {
        return this.repository.save(bankTransfer);
    }


}
