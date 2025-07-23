package com.jcondotta.bank_account_transfers.infrastructure.adapters.client.lookup_bank_account.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LookupBankAccountResponseCdo(@JsonProperty("bankAccount") BankAccountCdo bankAccountCdo) {

    public static LookupBankAccountResponseCdo of(BankAccountCdo bankAccountCdo) {
        return new LookupBankAccountResponseCdo(bankAccountCdo);
    }
}