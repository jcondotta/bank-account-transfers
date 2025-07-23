package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;

import java.util.Objects;

public record InternalAccountIdIdentifier(BankAccountId bankAccountId) implements InternalPartyIdentifier {

    public InternalAccountIdIdentifier {
        Objects.requireNonNull(bankAccountId, BankAccountId.ID_NOT_NULL_MESSAGE);
    }

    @Override
    public String asString() {
        return bankAccountId.toString();
    }

    public static InternalAccountIdIdentifier of(BankAccountId bankAccountId) {
        return new InternalAccountIdIdentifier(bankAccountId);
    }
}
