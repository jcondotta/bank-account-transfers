package com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.model;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartySender;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;

public interface RequestInternalTransferCommand {
    InternalPartySender partySender();
    InternalPartyRecipient partyRecipient();
    MonetaryAmount monetaryAmount();
    String reference();
}