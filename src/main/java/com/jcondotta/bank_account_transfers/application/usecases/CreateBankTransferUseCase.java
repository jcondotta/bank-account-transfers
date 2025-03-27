package com.jcondotta.bank_account_transfers.application.usecases;

import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.CreateBankTransferRequest;

public interface CreateBankTransferUseCase {
    DoubleEntryTransactionDTO createBankTransfer(CreateBankTransferRequest request);
}
