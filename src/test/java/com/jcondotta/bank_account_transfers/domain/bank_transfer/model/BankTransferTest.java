package com.jcondotta.bank_account_transfers.domain.bank_transfer.model;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartySender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.InternalTransferEntry;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestClockConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankTransferTest {

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");

    private static final BankAccountId SENDER_ACCOUNT_ID = BankAccountId.newId();
    private static final BankAccountId RECIPIENT_ACCOUNT_ID = BankAccountId.newId();

    private static final String TRANSFER_REFERENCE = "transfer reference";

    private final Clock clock = TestClockConfig.testFixedClock;

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldInitiateInternalTransfer_whenAllParamsAreValid(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var internalBankTransfer = BankTransfer.initiateInternalTransfer(
            SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, monetaryAmount, TRANSFER_REFERENCE, clock
        );

        assertThat(internalBankTransfer)
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
    void shouldThrowNullPointerException_whenInternalTransferSenderAccountIdIsNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        assertThatThrownBy(() -> BankTransfer.initiateInternalTransfer(null, RECIPIENT_ACCOUNT_ID, monetaryAmount, TRANSFER_REFERENCE, clock))
        .isInstanceOf(NullPointerException.class)
        .hasMessage(PartySender.SENDER_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenInternalTransferRecipientAccountIdIsNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        assertThatThrownBy(() -> BankTransfer.initiateInternalTransfer(SENDER_ACCOUNT_ID, null, monetaryAmount, TRANSFER_REFERENCE, clock))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @Test
    void shouldThrowNullPointerException_whenInternalTransferAmountIsNull() {
        assertThatThrownBy(() -> BankTransfer.initiateInternalTransfer(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, null, TRANSFER_REFERENCE, clock))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.BankTransfer.MONETARY_AMOUNT_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenInternalTransferClockIsNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        assertThatThrownBy(() -> BankTransfer.initiateInternalTransfer(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, monetaryAmount, TRANSFER_REFERENCE, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(OccurredAt.CLOCK_NOT_NULL_MESSAGE);
    }
}