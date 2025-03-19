package com.jcondotta.bank_account_transfers.domain;

public class BankAccountNotFoundException extends RuntimeException {

//    private final UUID bankAccountId;

    public BankAccountNotFoundException(String message) {
        super(message);
//        this.bankAccountId = bankAccountId;
    }

//    public UUID getBankAccountId() {
//        return bankAccountId;
//    }
}