package com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.mapper;

import com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.model.RequestInternalTransferCommand;
import com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.model.RequestInternalTransferFromAccountIdToIbanCommand;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.events.InternalTransferRequestedEvent;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt;
import org.mapstruct.Mapper;

import java.time.Clock;

@Mapper(componentModel = "spring")
public interface RequestInternalTransferCommandMapper {

    default InternalTransferRequestedEvent toEvent(RequestInternalTransferCommand command, Clock clock) {
        return InternalTransferRequestedEvent.of(
            command.partySender(),
            command.partyRecipient(),
            command.monetaryAmount(),
            command.reference(),
            OccurredAt.now(clock)
        );
    }
}
