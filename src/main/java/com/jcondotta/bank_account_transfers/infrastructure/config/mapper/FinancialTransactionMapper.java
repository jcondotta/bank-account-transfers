package com.jcondotta.bank_account_transfers.infrastructure.config.mapper;

import com.jcondotta.bank_account_transfers.domain.BankTransfer;
import com.jcondotta.bank_account_transfers.domain.FinancialTransaction;
import com.jcondotta.bank_account_transfers.domain.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface FinancialTransactionMapper {

    @Mapping(target = "bankTransfer", ignore = true)
    FinancialTransaction toEntity(BankTransfer bankTransfer, UUID bankAccountId,
                                  TransactionType transactionType, BigDecimal amount,
                                  String currency);
}