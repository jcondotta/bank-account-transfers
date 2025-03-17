package com.jcondotta.bank_account_transfers;

import java.util.UUID;

public enum TestBankAccount {

    JEFFERSON(
            "01920bff-1338-7efd-ade6-e9128debe5d4",
            "Jefferson Condotta",
            "ES3801283316232166447417"),

    PATRIZIO(
            "01921f7f-5672-70ac-8c7e-6d7a941706cb",
            "Patrizio Condotta",
            "IT93Q0300203280175171887193");

    private final String bankAccountId;
    private final String accountHolderName;
    private final String iban;

    TestBankAccount(String bankAccountId, String accountHolderName, String iban) {
        this.bankAccountId = bankAccountId;
        this.accountHolderName = accountHolderName;
        this.iban = iban;
    }

    public UUID getBankAccountId() {
        return UUID.fromString(bankAccountId);
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getIban() {
        return iban;
    }
}