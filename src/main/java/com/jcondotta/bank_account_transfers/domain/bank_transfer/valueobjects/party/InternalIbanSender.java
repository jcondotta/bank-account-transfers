package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalAccountIbanIdentifier;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalPartyIdentifier;

import java.util.Objects;

public record InternalIbanSender(Iban iban) implements InternalPartySender{

    public InternalIbanSender {
        Objects.requireNonNull(iban, PartySender.SENDER_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    public static InternalIbanSender of(Iban iban) {
        return new InternalIbanSender(iban);
    }

    public static InternalIbanSender of(String value) {
        return new InternalIbanSender(Iban.of(value));
    }

    @Override
    public InternalPartyIdentifier identifier() {
        return InternalAccountIbanIdentifier.of(iban);
    }
}