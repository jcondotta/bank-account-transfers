package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier;

public sealed interface InternalPartyIdentifier permits InternalAccountIdIdentifier, InternalAccountIbanIdentifier {
    String asString();
}