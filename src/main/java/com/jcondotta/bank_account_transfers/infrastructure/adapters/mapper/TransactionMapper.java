package com.jcondotta.bank_account_transfers.infrastructure.adapters.mapper;

import com.jcondotta.bank_account_transfers.domain.models.Transaction;
import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction.TransactionDTO;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction.CreateTransactionRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.UUID;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now(clock))")
    Transaction toEntity(UUID bankAccountId, TransactionType transactionType, BigDecimal amount, String currency, String reference, @Context Clock clock);

    default Transaction toEntity(CreateTransactionRequest request, @Context Clock clock) {
        return toEntity(
                request.bankAccountId(),
                request.monetaryMovement().transactionType(),
                request.monetaryMovement().amount(),
                request.monetaryMovement().currency(),
                request.reference(),
                clock);
    }

    TransactionDTO toDTO(Transaction transaction);
}