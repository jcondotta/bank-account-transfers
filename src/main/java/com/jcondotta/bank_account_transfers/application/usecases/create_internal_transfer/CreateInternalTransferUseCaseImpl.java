package com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer;

import com.jcondotta.bank_account_transfers.application.ports.output.repository.CreateBankTransferRepository;
import com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer.model.CreateInternalTransferCommand;
import com.jcondotta.bank_account_transfers.domain.bank_account.model.BankAccount;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.model.BankTransfer;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalParty;
import com.jcondotta.bank_account_transfers.infrastructure.facade.lookup_bank_account.LookupBankAccountFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Component
@AllArgsConstructor
public class CreateInternalTransferUseCaseImpl implements CreateInternalTransferUseCase {

    private final LookupBankAccountFacade lookupBankAccountFacade;
    private final CreateBankTransferRepository createBankTransferRepository;
    private final ExecutorService executorService;
    private final Clock clock;

    @Override
    public void execute(CreateInternalTransferCommand command) {
        var bankAccountSenderFuture = resolveInternalParty(command.partySender());
        var bankAccountRecipientFuture = resolveInternalParty(command.partyRecipient());

        CompletableFuture.allOf(bankAccountSenderFuture, bankAccountRecipientFuture).join();

        BankAccount accountSender = bankAccountSenderFuture.join();
        BankAccount accountRecipient = bankAccountRecipientFuture.join();

        var bankTransfer = BankTransfer.initiateInternalTransfer(
            accountSender.bankAccountId(), accountRecipient.bankAccountId(), command.monetaryAmount(), command.reference(), clock
        );

        createBankTransferRepository.saveBankTransfer(bankTransfer);
    }

    private CompletableFuture<BankAccount> resolveInternalParty(InternalParty party) {
        return lookupBankAccountFacade.resolveAsync(party.identifier(), executorService);
    }
}
