package com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.model;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountSender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalIbanRecipient;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;

public record RequestInternalTransferFromAccountIdToIbanCommand(
    InternalAccountSender partySender,
    InternalIbanRecipient partyRecipient,
    MonetaryAmount monetaryAmount,
    String reference

) implements RequestInternalTransferCommand {

    public RequestInternalTransferFromAccountIdToIbanCommand {
        if (partySender == null) {
            throw new NullPointerException("Sender account ID must not be null");
        }
        if (partyRecipient == null) {
            throw new NullPointerException("Recipient IBAN must not be null");
        }
        if (monetaryAmount == null) {
            throw new NullPointerException("Monetary amount must not be null");
        }
    }

    public static RequestInternalTransferFromAccountIdToIbanCommand of(InternalAccountSender partySender, InternalIbanRecipient partyRecipient, MonetaryAmount monetaryAmount, String reference) {
        return new RequestInternalTransferFromAccountIdToIbanCommand(partySender, partyRecipient, monetaryAmount, reference);
    }

    public static RequestInternalTransferFromAccountIdToIbanCommand of(BankAccountId senderAccountId, Iban recipientIban, MonetaryAmount monetaryAmount, String reference) {
        return new RequestInternalTransferFromAccountIdToIbanCommand(InternalAccountSender.of(senderAccountId), InternalIbanRecipient.of(recipientIban), monetaryAmount, reference);
    }
}
