package com.jcondotta.bank_account_transfers.application.usecases;

import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryAmount;

import java.util.UUID;

public interface CreateDoubleEntryTransactionUseCase {
    DoubleEntryTransactionDTO createDoubleEntryTransaction(UUID senderBankAccountId, UUID recipientBankAccountId, MonetaryAmount monetaryAmount, String reference);
}
