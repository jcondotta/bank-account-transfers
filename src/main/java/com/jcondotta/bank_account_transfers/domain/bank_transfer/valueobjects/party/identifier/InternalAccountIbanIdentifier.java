package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;

public record InternalAccountIbanIdentifier(Iban iban) implements InternalPartyIdentifier {

    @Override
    public String asString() {
        return iban.value();
    }

    public static InternalAccountIbanIdentifier of(Iban iban) {
        return new InternalAccountIbanIdentifier(iban);
    }
}
