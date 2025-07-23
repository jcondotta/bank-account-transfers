package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;

public sealed interface SepaParty permits SepaPartySender, SepaPartyRecipient {
    Iban iban();
    PartyName name();
}
