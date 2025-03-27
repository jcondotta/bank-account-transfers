package com.jcondotta.bank_account_transfers.application.usecases;

import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction.TransactionDTO;

import java.util.Objects;

import static com.jcondotta.bank_account_transfers.application.usecases.DoubleEntryTransactionDTO.DoubleEntryTransactionDTOImpl;

public sealed interface DoubleEntryTransactionDTO permits DoubleEntryTransactionDTOImpl {

    TransactionDTO debit();
    TransactionDTO credit();

    static DoubleEntryTransactionDTO of(TransactionDTO debit, TransactionDTO credit) {
        Objects.requireNonNull(debit, "transaction.debit.notNull");
        Objects.requireNonNull(credit, "transaction.credit.notNull");

        return new DoubleEntryTransactionDTOImpl(debit, credit);
    }

    record DoubleEntryTransactionDTOImpl(TransactionDTO debit, TransactionDTO credit)
            implements DoubleEntryTransactionDTO {}
}