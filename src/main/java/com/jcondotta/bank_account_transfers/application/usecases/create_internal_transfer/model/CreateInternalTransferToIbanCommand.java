package com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer.model;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountSender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalIbanRecipient;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;

import java.util.Objects;

public record CreateInternalTransferToIbanCommand(
    InternalAccountSender partySender,
    InternalIbanRecipient partyRecipient,
    MonetaryAmount monetaryAmount,
    String reference
) implements CreateInternalTransferCommand {

    public CreateInternalTransferToIbanCommand {
        Objects.requireNonNull(partySender, DomainErrorsMessages.BankTransfer.SENDER_ACCOUNT_ID_NOT_NULL);
        Objects.requireNonNull(partyRecipient, DomainErrorsMessages.BankTransfer.RECIPIENT_IBAN_NOT_NULL);
        Objects.requireNonNull(monetaryAmount, DomainErrorsMessages.BankTransfer.MONETARY_AMOUNT_NOT_NULL);
    }

    public static CreateInternalTransferToIbanCommand of(
        InternalAccountSender partySender,
        InternalIbanRecipient partyRecipient,
        MonetaryAmount monetaryAmount,
        String reference) {

        return new CreateInternalTransferToIbanCommand(partySender, partyRecipient, monetaryAmount, reference);
    }

    public static CreateInternalTransferToIbanCommand of(BankAccountId senderAccountId, Iban recipientiban, MonetaryAmount monetaryAmount, String reference) {
        return of(InternalAccountSender.of(senderAccountId), InternalIbanRecipient.of(recipientiban), monetaryAmount, reference);
    }

    public static CreateInternalTransferToIbanCommand of(BankAccountId senderAccountId, Iban recipientIban, MonetaryAmount monetaryAmount) {
        return of(senderAccountId, recipientIban, monetaryAmount, null);
    }
}
