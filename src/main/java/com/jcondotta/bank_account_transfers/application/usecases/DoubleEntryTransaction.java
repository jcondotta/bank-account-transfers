package com.jcondotta.bank_account_transfers.application.usecases;

import com.jcondotta.bank_account_transfers.domain.models.Transaction;

import java.util.Objects;

import static com.jcondotta.bank_account_transfers.application.usecases.DoubleEntryTransaction.DoubleEntryTransactionImpl;

public sealed interface DoubleEntryTransaction permits DoubleEntryTransactionImpl {

    Transaction debit();
    Transaction credit();

    static DoubleEntryTransaction of(Transaction debit, Transaction credit) {
        Objects.requireNonNull(debit, "transaction.debit.notNull");
        Objects.requireNonNull(credit, "transaction.credit.notNull");

        return new DoubleEntryTransactionImpl(debit, credit);
    }

    record DoubleEntryTransactionImpl(Transaction debit, Transaction credit)
            implements DoubleEntryTransaction {}
}