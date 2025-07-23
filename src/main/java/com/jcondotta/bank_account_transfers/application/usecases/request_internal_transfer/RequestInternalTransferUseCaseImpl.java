package com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer;

import com.jcondotta.bank_account_transfers.application.ports.output.messaging.RequestInternalTransferEventProducer;
import com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.mapper.RequestInternalTransferCommandMapper;
import com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.model.RequestInternalTransferCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@AllArgsConstructor
public class RequestInternalTransferUseCaseImpl implements RequestInternalTransferUseCase {

    private final RequestInternalTransferEventProducer eventProducer;
    private final RequestInternalTransferCommandMapper commandMapper;
    private final Clock clock;

    @Override
    public void execute(RequestInternalTransferCommand command) {
        var transferRequestedEvent = commandMapper.toEvent(command, clock);
        eventProducer.publish(transferRequestedEvent);
    }
}
