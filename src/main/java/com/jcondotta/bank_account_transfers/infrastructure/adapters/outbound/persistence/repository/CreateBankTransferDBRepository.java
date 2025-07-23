package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.repository;

import com.jcondotta.bank_account_transfers.application.ports.output.repository.CreateBankTransferRepository;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.model.BankTransfer;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.entity.BankTransferEntity;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.mapper.BankTransferEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CreateBankTransferDBRepository implements CreateBankTransferRepository {

    private final SpringBankTransferRepository springBankTransferRepository;
    private final BankTransferEntityMapper bankTransferEntityMapper;

    @Override
    public void saveBankTransfer(BankTransfer bankTransfer) {
        List<BankTransferEntity> entities = bankTransferEntityMapper.toEntities(bankTransfer);

        springBankTransferRepository.saveAll(entities);
    }
}
