package com.jcondotta.bank_account_transfers.domain.bank_transfer.exceptions.iban;

public class IbanNotFromSepaCountryException extends RuntimeException {

    public IbanNotFromSepaCountryException(String iban) {
        super("IBAN " + iban + " não pertence a um país participante da zona SEPA.");
    }
}
