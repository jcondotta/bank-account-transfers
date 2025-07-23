package com.jcondotta.bank_account_transfers.application.ports.output.repository;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.model.BankTransfer;

public interface CreateBankTransferRepository {

    void saveBankTransfer(BankTransfer bankTransfer);
}
