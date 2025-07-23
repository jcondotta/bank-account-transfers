package com.jcondotta.bank_account_transfers.domain.bank_account.exceptions;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.exceptions.ResourceNotFoundException;
import lombok.Getter;

@Getter
public class BankAccountNotFoundException extends ResourceNotFoundException {

    public static final String BANK_ACCOUNT_NOT_FOUND_TEMPLATE = "bankAccount.notFound";

    public BankAccountNotFoundException(BankAccountId bankAccountId) {
        super(BANK_ACCOUNT_NOT_FOUND_TEMPLATE, bankAccountId.value());
    }

    public BankAccountNotFoundException(Iban iban) {
        super(BANK_ACCOUNT_NOT_FOUND_TEMPLATE, iban.value());
    }
}
