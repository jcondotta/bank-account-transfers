package com.jcondotta.bank_account_transfers.domain.bank_account.model;

import com.jcondotta.bank_account_transfers.domain.bank_account.enums.AccountStatus;
import com.jcondotta.bank_account_transfers.domain.bank_account.enums.AccountType;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public record BankAccount(
    BankAccountId bankAccountId,
    AccountType accountType,
    Currency currency,
    AccountStatus status,
    Iban iban,
    OccurredAt createdAt)
{

    public BankAccount {
        requireNonNull(bankAccountId, "bankAccountId must not be null.");
        requireNonNull(accountType, "accountType must not be null.");
        requireNonNull(currency, "currency must not be null.");
        requireNonNull(status, "status must not be null.");
        requireNonNull(iban, "iban must not be null.");
        requireNonNull(createdAt, "createdAt must not be null.");

    }

    public static BankAccount of(BankAccountId bankAccountId, AccountType accountType, Currency currency, AccountStatus status, Iban iban, OccurredAt createdAt) {
        return new BankAccount(bankAccountId, accountType, currency, status, iban, createdAt);
    }
}