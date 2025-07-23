package com.jcondotta.bank_account_transfers.infrastructure.facade.lookup_bank_account;

import com.jcondotta.bank_account_transfers.domain.bank_account.model.BankAccount;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalAccountIbanIdentifier;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalAccountIdIdentifier;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalPartyIdentifier;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface LookupBankAccountFacade {

    BankAccount byId(BankAccountId bankAccountId);

    default CompletableFuture<BankAccount> byIdAsync(BankAccountId bankAccountId, Executor executor) {
        return CompletableFuture.supplyAsync(() -> this.byId(bankAccountId), executor);
    }

    BankAccount byIban(Iban iban);

    default CompletableFuture<BankAccount> byIbanAsync(Iban iban, Executor executor) {
        return CompletableFuture.supplyAsync(() -> this.byIban(iban), executor);
    }

    default BankAccount resolve(InternalPartyIdentifier partyIdentifier) {
        return switch (partyIdentifier) {
            case InternalAccountIdIdentifier id -> byId(id.bankAccountId());
            case InternalAccountIbanIdentifier id -> byIban(id.iban());
        };
    }

    //TODO melhorar a execucao de completable future com excetion handlers
    default CompletableFuture<BankAccount> resolveAsync(InternalPartyIdentifier partyIdentifier, Executor executor) {
        return CompletableFuture.supplyAsync(() -> this.resolve(partyIdentifier), executor)
            .exceptionally(ex -> {
                throw new RuntimeException("Failed to resolve bank account for identifier: " + partyIdentifier.asString(), ex);
            });
    }
}