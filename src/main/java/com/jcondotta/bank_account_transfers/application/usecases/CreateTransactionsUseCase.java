package com.jcondotta.bank_account_transfers.application.usecases;

import java.math.BigDecimal;
import java.util.UUID;

public interface CreateTransactionsUseCase {
    void createTransactions(UUID bankTransferId, UUID senderBankAccountId, UUID recipientAccountId, BigDecimal amount);
}
