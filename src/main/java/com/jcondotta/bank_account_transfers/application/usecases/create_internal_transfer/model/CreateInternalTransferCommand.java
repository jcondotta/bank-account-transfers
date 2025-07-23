package com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer.model;

import com.jcondotta.bank_account_transfers.application.usecases.shared.model.CreateTransferCommand;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartySender;

public interface CreateInternalTransferCommand extends CreateTransferCommand {

    @Override
    InternalPartySender partySender();

    @Override
    InternalPartyRecipient partyRecipient();
}