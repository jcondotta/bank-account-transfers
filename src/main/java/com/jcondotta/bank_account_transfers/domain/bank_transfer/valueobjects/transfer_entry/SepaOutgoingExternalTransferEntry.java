package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.exceptions.OutgoingTransferMustBeDebitException;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountSender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartySender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.SepaPartyRecipient;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryMovement;

import static java.util.Objects.requireNonNull;

public record SepaOutgoingExternalTransferEntry(
    InternalAccountSender partySender,
    SepaPartyRecipient partyRecipient,
    MonetaryMovement monetaryMovement
) implements TransferEntry {

    public SepaOutgoingExternalTransferEntry {
        requireNonNull(partySender, PartySender.SENDER_IDENTIFIER_NOT_NULL_MESSAGE);
        requireNonNull(partyRecipient, PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
        requireNonNull(monetaryMovement, TransferEntry.MONETARY_MOVEMENT_NOT_NULL_MESSAGE);

        if(!monetaryMovement.isDebit()){
            throw new OutgoingTransferMustBeDebitException(monetaryMovement.movementType());
        }
    }

    public static SepaOutgoingExternalTransferEntry of(InternalAccountSender partySender, SepaPartyRecipient partyRecipient, MonetaryAmount monetaryAmount) {
        return new SepaOutgoingExternalTransferEntry(partySender, partyRecipient, MonetaryMovement.ofDebit(monetaryAmount));
    }
}
