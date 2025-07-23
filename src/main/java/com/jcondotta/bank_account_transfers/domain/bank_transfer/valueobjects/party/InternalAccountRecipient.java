package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalAccountIdIdentifier;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalPartyIdentifier;

import java.util.Objects;
import java.util.UUID;

public record InternalAccountRecipient(BankAccountId bankAccountId) implements InternalPartyRecipient {

    public InternalAccountRecipient {
        Objects.requireNonNull(bankAccountId, PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @Override
    public InternalPartyIdentifier identifier() {
        return InternalAccountIdIdentifier.of(bankAccountId);
    }

    public static InternalAccountRecipient of(BankAccountId bankAccountId) {
        return new InternalAccountRecipient(bankAccountId);
    }

    public static InternalAccountRecipient of(UUID bankAccountId) {
        return new InternalAccountRecipient(BankAccountId.of(bankAccountId));
    }
}