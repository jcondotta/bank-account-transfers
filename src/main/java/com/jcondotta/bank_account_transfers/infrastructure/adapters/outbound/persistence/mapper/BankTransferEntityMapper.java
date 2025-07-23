package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.mapper;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.model.BankTransfer;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.InternalTransferEntry;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.SepaIncomingExternalTransferEntry;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.SepaOutgoingExternalTransferEntry;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.entity.BankTransferEntity;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.entity.CreatedAtEmbeddable;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.entity.MonetaryAmountEmbeddable;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.enums.TransferStatusEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BankTransferEntityMapper {

    public List<BankTransferEntity> toEntities(BankTransfer bankTransfer) {
        return bankTransfer.transferEntries().stream()
            .map(entry -> {
                var entity = new BankTransferEntity();
                entity.setBankTransferId(UUID.randomUUID());
                entity.setBankTransferGroupId(bankTransfer.bankTransferId().value());
                entity.setTransferType(TransferTypeEntityMapper.toEntity(bankTransfer.transferType()));
                entity.setStatus(TransferStatusEntity.COMPLETED);
                entity.setMovementType(MovementTypeEntityMapper.toEntity(entry.movementType()));
                entity.setMonetaryAmount(MonetaryAmountEmbeddable.of(entry.amount(), entry.currency().name()));
                entity.setReference(bankTransfer.reference());
                entity.setCreatedAt(CreatedAtEmbeddable.of(bankTransfer.occurredAt().asZonedDateTime()));

                if (entry instanceof InternalTransferEntry internalEntry) {
                    entity.setSenderAccountId(internalEntry.partySender().bankAccountId().value());
                    entity.setRecipientAccountId(internalEntry.partyRecipient().bankAccountId().value());
                }

                if (entry instanceof SepaIncomingExternalTransferEntry incomingEntry) {
                    entity.setSenderAccountId(null); // talvez venha de fora, ou deixe nulo
//                    entity.setTargetAccountId(incomingEntry.recipientIdentifier().bankAccountId().value());
//                    entity.setReference(incomingEntry.partySender().senderIban().toString());
                }

                if (entry instanceof SepaOutgoingExternalTransferEntry outgoingEntry) {
                    entity.setSenderAccountId(outgoingEntry.partySender().bankAccountId().value());
                    entity.setRecipientAccountId(null); // externo
//                    entity.setReference(outgoingEntry.recipientIdentifier().recipientIban().value());
                }

                return entity;
            })
            .toList();
    }
}
