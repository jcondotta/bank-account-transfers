package com.jcondotta.bank_account_transfers.application.usecases;

import com.jcondotta.bank_account_transfers.application.services.BankTransferDTO;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.CreateBankTransferRequest;

import java.util.UUID;

public interface CreateBankTransferUseCase {

    BankTransferDTO createBankTransfer(UUID idempotencyKey, CreateBankTransferRequest request);

}
