package com.jcondotta.bank_account_transfers.infrastructure.config.mapper;

import com.jcondotta.bank_account_transfers.domain.BankTransfer;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Clock;
import java.util.UUID;

@Mapper
public interface BankTransferMapper {

    BankTransferMapper INSTANCE = Mappers.getMapper(BankTransferMapper.class);

    @Mapping(target = "createdAt", expression = "java(Instant.now(clock))")
    @Mapping(target = "financialTransactions", ignore = true)
    BankTransfer toEntity(UUID idempotencyKey, String reference, @Context Clock clock);
}