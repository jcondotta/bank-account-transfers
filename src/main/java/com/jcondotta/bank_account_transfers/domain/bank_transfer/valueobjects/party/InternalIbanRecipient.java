package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalAccountIbanIdentifier;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalPartyIdentifier;

import java.util.Objects;

public record InternalIbanRecipient(Iban iban) implements InternalPartyRecipient {

    public InternalIbanRecipient {
        Objects.requireNonNull(iban, PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @Override
    public InternalPartyIdentifier identifier() {
        return InternalAccountIbanIdentifier.of(iban);
    }

    public static InternalIbanRecipient of(Iban iban) {
        return new InternalIbanRecipient(iban);
    }

    public static InternalIbanRecipient of(String value) {
        return new InternalIbanRecipient(Iban.of(value));
    }
}