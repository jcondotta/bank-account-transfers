package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalPartyIdentifier;

public sealed interface InternalParty permits InternalPartySender, InternalPartyRecipient {

    InternalPartyIdentifier identifier();

}