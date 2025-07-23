package com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer;

import com.jcondotta.bank_account_transfers.application.TestAccountDetails;
import com.jcondotta.bank_account_transfers.application.ports.output.repository.CreateBankTransferRepository;
import com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer.model.CreateInternalTransferToIbanCommand;
import com.jcondotta.bank_account_transfers.domain.bank_account.exceptions.BankAccountNotFoundException;
import com.jcondotta.bank_account_transfers.domain.bank_account.model.BankAccount;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.model.BankTransfer;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountSender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalIbanRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalAccountIbanIdentifier;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier.InternalAccountIdIdentifier;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.InternalTransferEntry;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestClockConfig;
import com.jcondotta.bank_account_transfers.infrastructure.facade.lookup_bank_account.LookupBankAccountFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateInternalTransferUseCaseImplTest {

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");

    private static final BankAccountId SENDER_ACCOUNT_ID = BankAccountId.newId();
    private static final BankAccountId RECIPIENT_ACCOUNT_ID = BankAccountId.newId();
    private static final Iban RECIPIENT_IBAN = Iban.of(TestAccountDetails.PATRIZIO.getIban());

    private static final InternalAccountSender INTERNAL_ACCOUNT_SENDER = InternalAccountSender.of(SENDER_ACCOUNT_ID);
    private static final InternalIbanRecipient INTERNAL_IBAN_RECIPIENT = InternalIbanRecipient.of(RECIPIENT_IBAN);

    private static final String TRANSFER_REFERENCE = "transfer reference";

    @Mock
    private LookupBankAccountFacade lookupBankAccountFacadeMock;

    @Mock
    private CreateBankTransferRepository createBankTransferRepositoryMock;

    @Mock
    private BankAccount accountSenderMock;

    @Mock
    private BankAccount accountRecipientMock;

    @Captor
    private ArgumentCaptor<BankTransfer> bankTransferCaptor;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Clock clock = TestClockConfig.testFixedClock;

    private CreateInternalTransferUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateInternalTransferUseCaseImpl(lookupBankAccountFacadeMock, createBankTransferRepositoryMock, executorService, clock);
    }

    @AfterEach
    void tearDown() {
        executorService.shutdownNow();
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateInternalBankTransfer_whenCommandIsValid(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var command = CreateInternalTransferToIbanCommand.of(INTERNAL_ACCOUNT_SENDER, INTERNAL_IBAN_RECIPIENT, monetaryAmount, TRANSFER_REFERENCE);

        when(accountSenderMock.bankAccountId()).thenReturn(SENDER_ACCOUNT_ID);
        when(accountRecipientMock.bankAccountId()).thenReturn(RECIPIENT_ACCOUNT_ID);

        when(lookupBankAccountFacadeMock.resolveAsync(command.partySender().identifier(), executorService))
            .thenReturn(CompletableFuture.completedFuture(accountSenderMock));
        when(lookupBankAccountFacadeMock.resolveAsync(command.partyRecipient().identifier(), executorService))
            .thenReturn(CompletableFuture.completedFuture(accountRecipientMock));

        useCase.execute(command);

        verify(lookupBankAccountFacadeMock).resolveAsync(any(InternalAccountIdIdentifier.class), any());
        verify(lookupBankAccountFacadeMock).resolveAsync(any(InternalAccountIbanIdentifier.class), any());

        verify(createBankTransferRepositoryMock).saveBankTransfer(bankTransferCaptor.capture());
        assertThat(bankTransferCaptor.getValue())
            .satisfies(bankTransfer -> {
                assertThat(bankTransfer.bankTransferId()).isNotNull();
                assertThat(bankTransfer.transferType().isInternal()).isTrue();
                assertThat(bankTransfer.transferEntries())
                    .hasSize(2)
                    .containsExactlyInAnyOrder(
                        InternalTransferEntry.ofDebit(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, monetaryAmount),
                        InternalTransferEntry.ofCredit(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, monetaryAmount)
                    );
                assertThat(bankTransfer.occurredAt().value()).isEqualTo(clock.instant());
                assertThat(bankTransfer.occurredAt().zoneId()).isEqualTo(clock.getZone());
                assertThat(bankTransfer.reference()).isEqualTo(TRANSFER_REFERENCE);
            });
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowBankAccountNotFoundException_whenInternalAccountSenderIsNotFound(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var command = CreateInternalTransferToIbanCommand.of(INTERNAL_ACCOUNT_SENDER, INTERNAL_IBAN_RECIPIENT, monetaryAmount, TRANSFER_REFERENCE);

        var bankAccountNotFoundException = new BankAccountNotFoundException(INTERNAL_ACCOUNT_SENDER.bankAccountId());

        when(lookupBankAccountFacadeMock.resolveAsync(command.partySender().identifier(), executorService))
            .thenReturn(CompletableFuture.failedFuture(bankAccountNotFoundException));

        when(lookupBankAccountFacadeMock.resolveAsync(command.partyRecipient().identifier(), executorService))
            .thenReturn(CompletableFuture.completedFuture(accountRecipientMock));

        assertThatThrownBy(() -> useCase.execute(command))
            .isInstanceOf(CompletionException.class)
            .hasCauseInstanceOf(BankAccountNotFoundException.class)
            .extracting(Throwable::getCause)
            .satisfies(cause -> assertThat(cause.getMessage()).isEqualTo(BankAccountNotFoundException.BANK_ACCOUNT_NOT_FOUND_TEMPLATE));

        verifyNoInteractions(createBankTransferRepositoryMock);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowException_whenRecipientBankAccountNotFound(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var command = CreateInternalTransferToIbanCommand.of(INTERNAL_ACCOUNT_SENDER, INTERNAL_IBAN_RECIPIENT, monetaryAmount, TRANSFER_REFERENCE);


        when(lookupBankAccountFacadeMock.resolveAsync(command.partySender().identifier(), executorService))
            .thenReturn(CompletableFuture.completedFuture(accountSenderMock));

        var bankAccountNotFoundException = new BankAccountNotFoundException(INTERNAL_IBAN_RECIPIENT.iban());
        when(lookupBankAccountFacadeMock.resolveAsync(command.partyRecipient().identifier(), executorService))
            .thenReturn(CompletableFuture.failedFuture(bankAccountNotFoundException));

        assertThatThrownBy(() -> useCase.execute(command))
            .isInstanceOf(CompletionException.class)
            .hasCauseInstanceOf(BankAccountNotFoundException.class)
            .extracting(Throwable::getCause)
            .satisfies(cause -> assertThat(cause.getMessage()).isEqualTo(BankAccountNotFoundException.BANK_ACCOUNT_NOT_FOUND_TEMPLATE));

        verifyNoInteractions(createBankTransferRepositoryMock);
    }
}
