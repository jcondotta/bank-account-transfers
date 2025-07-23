package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.exceptions.IncomingTransferMustBeCreditException;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartySender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.SepaPartySender;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryMovement;

import static java.util.Objects.requireNonNull;

public record SepaIncomingExternalTransferEntry(
    SepaPartySender partySender,
    InternalPartyRecipient partyRecipient,
    MonetaryMovement monetaryMovement
) implements TransferEntry {

    public SepaIncomingExternalTransferEntry {
        requireNonNull(partySender, PartySender.SENDER_IDENTIFIER_NOT_NULL_MESSAGE);
        requireNonNull(partyRecipient, PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
        requireNonNull(monetaryMovement, TransferEntry.MONETARY_MOVEMENT_NOT_NULL_MESSAGE);

        if(!monetaryMovement.isCredit()){
            throw new IncomingTransferMustBeCreditException(monetaryMovement.movementType());
        }
    }

    public static SepaIncomingExternalTransferEntry of(SepaPartySender partySender, InternalPartyRecipient partyRecipient, MonetaryAmount monetaryAmount) {
        return new SepaIncomingExternalTransferEntry(partySender, partyRecipient, MonetaryMovement.ofCredit(monetaryAmount));
    }
}