package com.jcondotta.bank_account_transfers.domain.bank_transfer.events;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartySender;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt;

public record InternalTransferRequestedEvent(
    InternalPartySender internalPartySender,
    InternalPartyRecipient internalPartyRecipient,
    MonetaryAmount monetaryAmount,
    String reference,
    OccurredAt requestedAt) implements DomainEvent<InternalTransferRequestedEvent>{

    public static InternalTransferRequestedEvent of(InternalPartySender internalPartySender, InternalPartyRecipient internalPartyRecipient, MonetaryAmount monetaryAmount, String reference, OccurredAt requestedAt) {
        return new InternalTransferRequestedEvent(internalPartySender, internalPartyRecipient, monetaryAmount, reference, requestedAt);
    }
}
