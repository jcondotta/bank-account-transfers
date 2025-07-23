package com.jcondotta.bank_account_transfers.infrastructure.adapters.client.lookup_bank_account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BankAccountCdo(
    UUID bankAccountId,
    AccountTypeCdo accountType,
    CurrencyCdo currency,
    String iban,
    AccountStatusCdo status,
    ZonedDateTime dateOfOpening
) {
    public static BankAccountCdo of(
        UUID bankAccountId,
        AccountTypeCdo accountType,
        CurrencyCdo currency,
        String iban,
        AccountStatusCdo status,
        ZonedDateTime dateOfOpening
    ) {
        return new BankAccountCdo(bankAccountId, accountType, currency, iban, status, dateOfOpening);
    }
}