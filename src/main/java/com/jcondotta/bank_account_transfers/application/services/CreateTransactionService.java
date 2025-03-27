package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.application.usecases.CreateTransactionUseCase;
import com.jcondotta.bank_account_transfers.domain.models.Transaction;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction.CreateTransactionRequest;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.mapper.TransactionMapper;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class CreateTransactionService implements CreateTransactionUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTransactionService.class);

    private final TransactionMapper transactionMapper;
    private final Validator validator;
    private final Clock clock;

    public CreateTransactionService(TransactionMapper transactionMapper, Validator validator, Clock clock) {
        this.transactionMapper = transactionMapper;
        this.validator = validator;
        this.clock = clock;
    }

    @Override
    public Transaction createTransaction(CreateTransactionRequest request) {
        request.validate(validator);

        LOGGER.atInfo().setMessage("Creating {} transaction for bankAccountId={}")
                .addArgument(request.monetaryMovement().transactionType())
                .addArgument(request.bankAccountId())
                .addKeyValue("bankAccountId", request.bankAccountId())
                .log();

        return transactionMapper.toEntity(request, clock);
    }
}
