package com.jcondotta.bank_account_transfers.application.usecases.shared.model;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartySender;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;

public interface CreateTransferCommand {
    PartySender partySender();
    PartyRecipient partyRecipient();
    MonetaryAmount monetaryAmount();
    String reference();
}