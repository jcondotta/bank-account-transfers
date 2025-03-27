package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.application.ports.outbound.persistence.TransactionRepositoryPort;
import com.jcondotta.bank_account_transfers.application.usecases.CreateDoubleEntryTransactionUseCase;
import com.jcondotta.bank_account_transfers.application.usecases.DoubleEntryTransactionDTO;
import com.jcondotta.bank_account_transfers.domain.models.Transaction;
import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryMovement;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction.CreateTransactionRequest;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.mapper.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreateDoubleEntryTransactionService implements CreateDoubleEntryTransactionUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDoubleEntryTransactionService.class);

    private final CreateTransactionService createTransactionService;
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final TransactionMapper transactionMapper;

    public CreateDoubleEntryTransactionService(CreateTransactionService createTransactionService, TransactionRepositoryPort transactionRepositoryPort, TransactionMapper transactionMapper) {
        this.createTransactionService = createTransactionService;
        this.transactionRepositoryPort = transactionRepositoryPort;
        this.transactionMapper = transactionMapper;
    }

    public DoubleEntryTransactionDTO createDoubleEntryTransaction(UUID senderBankAccountId, UUID recipientBankAccountId, MonetaryAmount monetaryAmount, String reference){
        LOGGER.atInfo().setMessage("Creating double entry transaction")
//                .addArgument(request.monetaryMovement().transactionType())
//                .addArgument(request.bankAccountId())
                .addKeyValue("senderBankAccountId", senderBankAccountId)
                .addKeyValue("recipientBankAccountId", recipientBankAccountId)
                .log();

        var debitTransaction = createTransaction(senderBankAccountId, MonetaryMovement.debit(monetaryAmount), reference);
        var creditTransaction = createTransaction(recipientBankAccountId, MonetaryMovement.credit(monetaryAmount), reference);

        transactionRepositoryPort.saveAll(List.of(debitTransaction, creditTransaction));

        LOGGER.atInfo().setMessage("Double entry transaction created successfully")
//                .addArgument(request.monetaryMovement().transactionType())
//                .addArgument(request.bankAccountId())
                .addKeyValue("senderBankAccountId", senderBankAccountId)
                .addKeyValue("recipientBankAccountId", recipientBankAccountId)
                .log();

        return DoubleEntryTransactionDTO.of(
                transactionMapper.toDTO(debitTransaction),
                transactionMapper.toDTO(creditTransaction)
        );
    }

    private Transaction createTransaction(UUID bankAccountId, MonetaryMovement monetaryMovement, String reference) {
        var createTransactionRequest = CreateTransactionRequest.builder()
                .bankAccountId(bankAccountId)
                .monetaryMovement(monetaryMovement)
                .reference(reference)
                .build();

        return createTransactionService.createTransaction(createTransactionRequest);
    }
}
