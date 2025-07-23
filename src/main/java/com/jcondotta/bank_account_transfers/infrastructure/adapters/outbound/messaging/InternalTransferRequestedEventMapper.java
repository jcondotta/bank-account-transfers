package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.messaging;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.events.InternalTransferRequestedEvent;
import com.jcondotta.bank_account_transfers.interfaces.kinesis.request_internal_transfer.model.RequestInternalTransferEvent;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InternalTransferRequestedEventMapper {

    default RequestInternalTransferEvent toKinesisEvent(InternalTransferRequestedEvent event){
        return new RequestInternalTransferEvent(
            UUID.fromString(event.internalPartySender().identifier().asString()),
            event.internalPartyRecipient().identifier().asString(),
            event.monetaryAmount().amount(),
            event.monetaryAmount().currency().name(),
            event.reference(),
            event.requestedAt().asZonedDateTime()
        );
    }
}
