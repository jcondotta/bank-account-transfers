package com.jcondotta.bank_account_transfers.application.usecases;

import com.jcondotta.bank_account_transfers.domain.models.Transaction;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction.CreateTransactionRequest;

public interface CreateTransactionUseCase {
    Transaction createTransaction(CreateTransactionRequest request);
}
