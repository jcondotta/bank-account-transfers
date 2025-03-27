package com.jcondotta.bank_account_transfers.domain.exceptions;

public class BankAccountNotFoundException extends ResourceNotFoundException {

    public BankAccountNotFoundException(String message) {
        super(message);
    }
}