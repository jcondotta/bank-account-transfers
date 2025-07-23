package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

public sealed interface PartySender
    extends Party
    permits InternalPartySender, ExternalPartySender {

    String SENDER_IDENTIFIER_NOT_NULL_MESSAGE = "sender's identifier must not be null.";
}