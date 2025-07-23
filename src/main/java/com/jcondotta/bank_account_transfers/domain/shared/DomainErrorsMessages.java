package com.jcondotta.bank_account_transfers.domain.shared;

public interface DomainErrorsMessages {

    interface BankTransfer {

        String SENDER_ACCOUNT_ID_NOT_NULL = "sender's account id must not be null.";
        String RECIPIENT_ACCOUNT_ID_NOT_NULL = "recipients's account id must not be null.";
        String RECIPIENT_IBAN_NOT_NULL = "recipients's iban must not be null.";

        String MONETARY_AMOUNT_NOT_NULL = "monetary amount must not be null.";
        String MONETARY_AMOUNT_POSITIVE = "monetary amount must not be negative.";

        String MONETARY_CURRENCY_NOT_NULL = "currency must not be null.";
        String MONETARY_CURRENCY_INVALID_FORMAT = "currency must be a valid ISO 4217 code (e.g., USD, EUR, GBP)";
        String REFERENCE_TOO_LONG = "reference must not be longer than 50 characters";
        String CLOCK_NOT_NULL = "reference must not be longer than 50 characters";
    }

    interface ExternalPartySender {
        String PARTY_NAME_NOT_NULL = "external party name must not be null.";
        String IBAN_NOT_NULL = "external party iban must not be null.";
    }

    interface ExternalPartyRecipient {
        String PARTY_NAME_NOT_NULL = "external party name must not be null.";
        String IBAN_NOT_NULL = "external party iban must not be null.";
    }

    interface SepaParty {
        String NAME_NOT_NULL = "name must not be null.";
        String IBAN_NOT_NULL = "iban must not be null.";
    }
}
