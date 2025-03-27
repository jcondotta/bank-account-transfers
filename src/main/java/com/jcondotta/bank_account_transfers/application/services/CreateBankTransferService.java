package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.application.usecases.BankAccountLookupUseCase;
import com.jcondotta.bank_account_transfers.application.usecases.CreateBankTransferUseCase;
import com.jcondotta.bank_account_transfers.application.usecases.CreateDoubleEntryTransactionUseCase;
import com.jcondotta.bank_account_transfers.application.usecases.DoubleEntryTransactionDTO;
import com.jcondotta.bank_account_transfers.domain.exceptions.BankAccountNotFoundException;
import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryAmount;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.CreateBankTransferRequest;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts.BankAccountDTO;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateBankTransferService implements CreateBankTransferUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankTransferService.class);

    private final CreateDoubleEntryTransactionUseCase createDoubleEntryTransactionUseCase;
    private final BankAccountLookupUseCase bankAccountLookupUseCase;
    private final Validator validator;

    public CreateBankTransferService(CreateDoubleEntryTransactionUseCase createDoubleEntryTransactionUseCase, BankAccountLookupUseCase bankAccountLookupUseCase, Validator validator) {
        this.createDoubleEntryTransactionUseCase = createDoubleEntryTransactionUseCase;
        this.bankAccountLookupUseCase = bankAccountLookupUseCase;
        this.validator = validator;
    }

    @Override
    public DoubleEntryTransactionDTO createBankTransfer(CreateBankTransferRequest request) {
        request.validate(validator);

        var monetaryAmount = MonetaryAmount.of(request.amount(), request.currency());
        UUID recipientBankAccountId = bankAccountLookupUseCase.findBankAccountByIban(request.recipientIban())
                .map(BankAccountDTO::bankAccountId)
                .orElseThrow(() -> new BankAccountNotFoundException("bankAccount.notFound"));

        return createDoubleEntryTransactionUseCase.createDoubleEntryTransaction(
                request.senderBankAccountId(),
                recipientBankAccountId,
                monetaryAmount,
                request.reference()
        );
    }
}
