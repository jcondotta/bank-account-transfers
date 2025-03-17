package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.cache;

public class BankAccountCacheKeys {

    private static final String BANK_ACCOUNT_IBAN_TEMPLATE = "bankAccount:bankAccountIban:%s";

    public static String bankAccountIbanKey(String bankAccountIban) {
        return String.format(BANK_ACCOUNT_IBAN_TEMPLATE, bankAccountIban);
    }
}
