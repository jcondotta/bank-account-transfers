package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban;

import java.util.Objects;

public record Iban(String value) {

    public static final String IBAN_VALUE_NOT_NULL_MESSAGE = "iban value must not be null.";
//    public static final String INVALID_FORMAT = "";

    public Iban {
        Objects.requireNonNull(value, IBAN_VALUE_NOT_NULL_MESSAGE);

//        value = value.replaceAll("\\s+", "").toUpperCase();
//
//        if (!IBANCheckDigit.IBAN_CHECK_DIGIT.isValid(value)) {
//            throw new IllegalArgumentException(DomainErrorsMessages.Iban.INVALID_FORMAT);
//        }

//        if (!isFromSepaCountry(value)) {
//            throw new IbanNotFromSepaCountryException(value);
//        }
    }

    public static Iban of(String value) {
        return new Iban(value);
    }

    public String countryCode() {
        return value.substring(0, 2).toUpperCase();
    }

    public boolean isFromSepaCountry() {
        return SepaCountry.isSepaCountry(countryCode());
    }

    @Override
    public String toString() {
        return value;
    }
}
