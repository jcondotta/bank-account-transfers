package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.application.usecases.BankAccountLookupUseCase;
import com.jcondotta.bank_account_transfers.domain.BankAccountNotFoundException;
import com.jcondotta.bank_account_transfers.domain.BankTransfer;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.CreateBankTransferRequest;
import com.jcondotta.bank_account_transfers.infrastructure.config.mapper.FinancialTransactionMapper;
import com.jcondotta.bank_account_transfers.infrastructure.config.mapper.InternalBankTransferMapper;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;

@Service
public class CreateInternalBankTransferService {

    private final InternalBankTransferMapper bankTransferMapper;
    private final FinancialTransactionMapper financialTransactionMapper;
    private final BankAccountLookupUseCase bankAccountLookupUseCase;
    private final Clock clock;

    public CreateInternalBankTransferService(InternalBankTransferMapper bankTransferMapper, FinancialTransactionMapper financialTransactionMapper, BankAccountLookupUseCase bankAccountLookupUseCase, Clock clock) {
        this.bankTransferMapper = bankTransferMapper;
        this.financialTransactionMapper = financialTransactionMapper;
        this.bankAccountLookupUseCase = bankAccountLookupUseCase;
        this.clock = clock;
    }

    public BankTransfer processTransaction(UUID idempotencyKey, CreateBankTransferRequest request) {
        var recipientBankAccountDTO = bankAccountLookupUseCase.findBankAccountByIban(request.recipientIban())
                .orElseThrow(() -> new BankAccountNotFoundException("bankAccount.notFound"));

        var bankTransfer2 = bankTransferMapper.toInternalBankTransfer(idempotencyKey, request.senderBankAccountId(), recipientBankAccountDTO.bankAccountId(), request.amount(), request.currency(), request.reference(), financialTransactionMapper, clock);
        return bankTransfer2;
    }
}
