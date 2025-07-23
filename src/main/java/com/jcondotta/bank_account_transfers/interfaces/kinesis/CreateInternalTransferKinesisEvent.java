package com.jcondotta.bank_account_transfers.interfaces.kinesis;

import com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer.model.CreateInternalTransferToIbanCommand;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountSender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalIbanRecipient;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record CreateInternalTransferKinesisEvent(
    UUID senderAccountId,
    String recipientIban,
    BigDecimal amount,
    String currency,
    String reference,
    ZonedDateTime requestedAt
) {

    public static CreateInternalTransferKinesisEvent of(
        UUID senderAccountId,
        String recipientIban,
        BigDecimal amount,
        Currency currency,
        String reference,
        ZonedDateTime requestedAt
    ) {
        return new CreateInternalTransferKinesisEvent(
            senderAccountId,
            recipientIban,
            amount,
            currency.name(),
            reference,
            requestedAt
        );
    }

    public CreateInternalTransferToIbanCommand toCommand() {
        return CreateInternalTransferToIbanCommand.of(
            InternalAccountSender.of(BankAccountId.of(senderAccountId)),
            InternalIbanRecipient.of(Iban.of(recipientIban)),
            MonetaryAmount.of(amount, Currency.valueOf(currency)),
            reference
        );
    }
}
