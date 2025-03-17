package com.jcondotta.bank_account_transfers.infrastructure.config.mapper;

import com.jcondotta.bank_account_transfers.domain.BankTransfer;
import com.jcondotta.bank_account_transfers.domain.TransactionType;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = { FinancialTransactionMapper.class })
public interface InternalBankTransferMapper extends BankTransferMapper {

    InternalBankTransferMapper INSTANCE = Mappers.getMapper(InternalBankTransferMapper.class);

    default BankTransfer toInternalBankTransfer(
            UUID idempotencyKey, UUID senderBankAccountId, UUID recipientBankAccountId, BigDecimal amount,
            String currency, String reference, FinancialTransactionMapper transactionMapper, @Context Clock clock) {

        var bankTransfer = toEntity(idempotencyKey, reference, clock);

        bankTransfer.addFinancialTransaction(transactionMapper.toEntity(
                bankTransfer, senderBankAccountId, TransactionType.DEBIT, amount.negate(), currency));

        bankTransfer.addFinancialTransaction(transactionMapper
                .toEntity(bankTransfer, recipientBankAccountId, TransactionType.CREDIT, amount, currency));

        return bankTransfer;
    }
}
