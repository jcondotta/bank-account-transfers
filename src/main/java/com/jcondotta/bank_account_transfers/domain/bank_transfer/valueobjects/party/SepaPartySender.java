package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;

import java.util.Objects;

public record SepaPartySender(Iban iban, PartyName name) implements SepaParty, ExternalPartySender {

    public SepaPartySender {
        Objects.requireNonNull(iban, DomainErrorsMessages.SepaParty.IBAN_NOT_NULL);
        Objects.requireNonNull(name, DomainErrorsMessages.SepaParty.NAME_NOT_NULL);
    }

    public static SepaPartySender of(Iban iban, PartyName name) {
        return new SepaPartySender(iban, name);
    }

    public static SepaPartySender of(String iban, String name) {
        return of(Iban.of(iban), PartyName.of(name));
    }
}