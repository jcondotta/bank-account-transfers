package com.jcondotta.bank_account_transfers.domain.bank_transfer.exceptions;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalPartySender;
import com.jcondotta.bank_account_transfers.domain.shared.exceptions.BusinessRuleException;

import java.util.Map;

public class IdenticalSenderAndRecipientException extends BusinessRuleException {

    public static final String MESSAGE_TEMPLATE = "bankTransfer.identicalSenderRecipient";

    private final InternalPartySender internalPartySender;
    private final InternalPartyRecipient internalPartyRecipient;

    public IdenticalSenderAndRecipientException(InternalPartySender internalPartySender, InternalPartyRecipient internalPartyRecipient) {
        super(MESSAGE_TEMPLATE);
        this.internalPartySender = internalPartySender;
        this.internalPartyRecipient = internalPartyRecipient;
    }

    @Override
    public String getType() {
        return "/errors/identical-sender-recipient";
    }

    @Override
    public Map<String, Object> getProperties() {
        return Map.of(
            "partySender", internalPartySender.toString(),
            "partyRecipient", internalPartyRecipient.toString()
        );
    }
}
