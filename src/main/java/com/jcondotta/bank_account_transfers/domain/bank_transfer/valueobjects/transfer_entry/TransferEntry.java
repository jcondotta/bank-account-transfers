package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartySender;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.enums.MovementType;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryMovement;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;

import java.math.BigDecimal;

public interface TransferEntry{

    String MONETARY_MOVEMENT_NOT_NULL_MESSAGE = "monetary movement must not be null.";

    PartySender partySender();
    PartyRecipient partyRecipient();
    MonetaryMovement monetaryMovement();

    default BigDecimal amount() {
        return monetaryMovement().amount();
    }

    default Currency currency() {
        return monetaryMovement().currency();
    }

    default MovementType movementType() {
        return monetaryMovement().movementType();
    }

    default boolean isDebit() {
        return monetaryMovement().isDebit();
    }

    default boolean isCredit() {
        return monetaryMovement().isCredit();
    }
}