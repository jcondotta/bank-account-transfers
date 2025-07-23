package com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer;

import com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.model.RequestInternalTransferCommand;

public interface RequestInternalTransferUseCase {

    void execute(RequestInternalTransferCommand command);
}