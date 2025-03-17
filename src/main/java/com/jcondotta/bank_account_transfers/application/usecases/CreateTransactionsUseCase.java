package com.jcondotta.bank_account_transfers.application.usecases;

import java.util.UUID;
import java.math.BigDecimal;

public interface CreateTransactionsUseCase {
    void createTransactions(UUID bankTransferId, UUID senderBankAccountId, UUID recipientAccountId, BigDecimal amount);
}
