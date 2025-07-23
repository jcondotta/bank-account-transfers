package com.jcondotta.bank_account_transfers.domain.bank_transfer.model;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.enums.TransferType;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.BankTransferId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.InternalTransferEntry;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.TransferEntry;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt;

import java.time.Clock;
import java.util.List;

public record BankTransfer(BankTransferId bankTransferId, List<TransferEntry> transferEntries, TransferType transferType, OccurredAt occurredAt, String reference){

    public BankTransfer {
//        requireNonNull(sourceAccountId, "sourceAccountId must not be null");
//        requireNonNull(clock, "clock must not be null");
//        requireNonNull(amount, "amount must not be null");
    }

    public static BankTransfer initiateInternalTransfer(BankAccountId senderAccountId, BankAccountId recipientAccountId, MonetaryAmount amount, String reference, Clock clock) {
        var entryDebit = InternalTransferEntry.ofDebit(senderAccountId, recipientAccountId, amount);
        var entryCredit = InternalTransferEntry.ofCredit(senderAccountId, recipientAccountId, amount);

        return new BankTransfer(
            BankTransferId.newId(),
            List.of(entryDebit, entryCredit),
            TransferType.INTERNAL,
            OccurredAt.now(clock),
            reference
        );
    }

//    public static BankTransfer receiveIncomingExternalTransfer(
//        ExternalPartySender3 externalPartySender,
//        BankAccountId partyRecipient,
//        MonetaryAmount amount,
//        String reference,
//        Clock clock
//    ) {
////        var internalPartyRecipient = InternalRecipientParty2.of(partyRecipient);
////        var creditEntry = SepaIncomingExternalTransferEntry.of(externalPartySender, internalPartyRecipient, amount);
////
////        return new BankTransfer(
////            BankTransferId.newId(),
////            List.of(creditEntry),
////            TransferType.INCOMING_EXTERNAL,
////            CreatedAt.now(clock),
////            reference
////        );
//
//        return null;
//    }

//    public static BankTransfer sendOutgoingExternalTransfer(
//        BankAccountId sourceAccountId,
//        SEPARecipient recipient,
//        MonetaryAmount amount,
//        String reference,
//        Clock clock
//    ) {
//        var senderIdentifier = InternalSenderParty2.of(sourceAccountId);
//        var debitEntry = SepaOutgoingExternalTransferEntry.of(senderIdentifier, recipient, amount);
//
//        return new BankTransfer(
//            BankTransferId.newId(),
//            List.of(debitEntry),
//            TransferType.OUTGOING_EXTERNAL,
//            CreatedAt.now(clock),
//            reference
//        );
//    }
}