package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.exceptions.IdenticalSenderAndRecipientException;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountSender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartySender;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.enums.MovementType;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryMovement;

import static java.util.Objects.requireNonNull;

public record InternalTransferEntry(InternalAccountSender partySender, InternalAccountRecipient partyRecipient, MonetaryMovement monetaryMovement)
    implements TransferEntry {

    public InternalTransferEntry {
        requireNonNull(partySender, PartySender.SENDER_IDENTIFIER_NOT_NULL_MESSAGE);
        requireNonNull(partyRecipient, PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
        requireNonNull(monetaryMovement, TransferEntry.MONETARY_MOVEMENT_NOT_NULL_MESSAGE);

        if (partySender.bankAccountId().equals(partyRecipient.bankAccountId())) {
            throw new IdenticalSenderAndRecipientException(partySender, partyRecipient);
        }
    }

    public static InternalTransferEntry of(BankAccountId senderAccountId, BankAccountId recipientAccountId, MovementType movementType, MonetaryAmount monetaryAmount) {
        return new InternalTransferEntry(
            InternalAccountSender.of(senderAccountId),
            InternalAccountRecipient.of(recipientAccountId),
            MonetaryMovement.of(movementType, monetaryAmount)
        );
    }

    public static InternalTransferEntry ofDebit(BankAccountId sourceAccountId, BankAccountId recipientAccountId, MonetaryAmount monetaryAmount) {
        return of(sourceAccountId, recipientAccountId, MovementType.DEBIT, monetaryAmount);
    }

    public static InternalTransferEntry ofCredit(BankAccountId sourceAccountId, BankAccountId recipientAccountId, MonetaryAmount monetaryAmount) {
        return of(sourceAccountId, recipientAccountId, MovementType.CREDIT, monetaryAmount);
    }
}
