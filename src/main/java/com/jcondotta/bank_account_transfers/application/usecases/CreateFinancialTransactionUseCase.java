package com.jcondotta.bank_account_transfers.application.usecases;

import com.jcondotta.bank_account_transfers.domain.FinancialTransaction;
import com.jcondotta.bank_account_transfers.domain.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public interface CreateFinancialTransactionUseCase {
    FinancialTransaction createTransaction(UUID bankTransferId, UUID accountId, TransactionType transactionType, BigDecimal amount);
}
