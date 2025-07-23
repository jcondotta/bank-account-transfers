package com.jcondotta.bank_account_transfers.application.ports.output.messaging;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.events.InternalTransferRequestedEvent;

public interface RequestInternalTransferEventProducer {

    void publish(InternalTransferRequestedEvent event);

}