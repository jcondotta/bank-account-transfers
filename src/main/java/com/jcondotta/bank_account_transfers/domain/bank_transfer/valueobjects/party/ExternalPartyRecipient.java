package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

public sealed interface ExternalPartyRecipient extends PartyRecipient permits SepaPartyRecipient { }
