package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.argument_provider.MovementTypeAndCurrencyArgumentsProvider;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.exceptions.IdenticalSenderAndRecipientException;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.InternalTransferEntry;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.transfer_entry.TransferEntry;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.enums.MovementType;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryMovement;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InternalTransferEntryTest {

    private static final BankAccountId SENDER_ACCOUNT_ID = BankAccountId.newId();
    private static final BankAccountId RECIPIENT_ACCOUNT_ID = BankAccountId.newId();

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");

    @ParameterizedTest
    @ArgumentsSource(MovementTypeAndCurrencyArgumentsProvider.class)
    void shouldCreateInternalTransferEntry_whenParamsAreValid(MovementType movementType, Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var monetaryMovement = MonetaryMovement.of(movementType, monetaryAmount);

        var internalPartySender = InternalAccountSender.of(SENDER_ACCOUNT_ID);
        var internalPartyRecipient = InternalAccountRecipient.of(RECIPIENT_ACCOUNT_ID);

        var internalTransferEntry = new InternalTransferEntry(internalPartySender, internalPartyRecipient, monetaryMovement);

        assertThat(internalTransferEntry)
            .satisfies(entry -> {
                assertThat(entry.partySender().bankAccountId()).isEqualTo(SENDER_ACCOUNT_ID);
                assertThat(entry.partyRecipient().bankAccountId()).isEqualTo(RECIPIENT_ACCOUNT_ID);
                assertThat(entry.monetaryMovement()).isEqualTo(monetaryMovement);
            });
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateInternalTransferEntry_whenUsingOfFactoryMethodWithDebit(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        var internalTransferEntry = InternalTransferEntry.ofDebit(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, monetaryAmount);

        assertThat(internalTransferEntry)
            .satisfies(entry -> {
                assertThat(entry.partySender().bankAccountId()).isEqualTo(SENDER_ACCOUNT_ID);
                assertThat(entry.partyRecipient().bankAccountId()).isEqualTo(RECIPIENT_ACCOUNT_ID);
                assertThat(entry.monetaryMovement().movementType()).isEqualTo(MovementType.DEBIT);
                assertThat(entry.monetaryMovement().monetaryAmount()).isEqualTo(monetaryAmount);
            });
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateInternalTransferEntry_whenUsingOfFactoryMethodWithCredit(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        var internalTransferEntry = InternalTransferEntry.ofCredit(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, monetaryAmount);

        assertThat(internalTransferEntry)
            .satisfies(entry -> {
                assertThat(entry.partySender().bankAccountId()).isEqualTo(SENDER_ACCOUNT_ID);
                assertThat(entry.partyRecipient().bankAccountId()).isEqualTo(RECIPIENT_ACCOUNT_ID);
                assertThat(entry.monetaryMovement().movementType()).isEqualTo(MovementType.CREDIT);
                assertThat(entry.monetaryMovement().monetaryAmount()).isEqualTo(monetaryAmount);
            });
    }

    @ParameterizedTest
    @ArgumentsSource(MovementTypeAndCurrencyArgumentsProvider.class)
    void shouldThrowException_whenSenderAndRecipientAreIdentical(MovementType movementType, Currency currency) {
        var accountId = BankAccountId.newId();
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var monetaryMovement = MonetaryMovement.of(movementType, monetaryAmount);

        var internalPartySender = InternalAccountSender.of(accountId);
        var internalPartyRecipient = InternalAccountRecipient.of(accountId);

        assertThatThrownBy(() -> new InternalTransferEntry(internalPartySender, internalPartyRecipient, monetaryMovement))
            .isInstanceOf(IdenticalSenderAndRecipientException.class)
            .hasMessage(IdenticalSenderAndRecipientException.MESSAGE_TEMPLATE);
    }

    @ParameterizedTest
    @ArgumentsSource(MovementTypeAndCurrencyArgumentsProvider.class)
    void shouldThrowNullPointerException_whenSenderIdentifierIsNull(MovementType movementType, Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var monetaryMovement = MonetaryMovement.of(movementType, monetaryAmount);

        var internalPartyRecipient = InternalAccountRecipient.of(RECIPIENT_ACCOUNT_ID);

        assertThatThrownBy(() -> new InternalTransferEntry(null, internalPartyRecipient, monetaryMovement))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartySender.SENDER_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @ArgumentsSource(MovementTypeAndCurrencyArgumentsProvider.class)
    void shouldThrowNullPointerException_whenRecipientIdentifierIsNull(MovementType movementType, Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        var monetaryMovement = MonetaryMovement.of(movementType, monetaryAmount);

        var internalPartySender = InternalAccountSender.of(SENDER_ACCOUNT_ID);

        assertThatThrownBy(() -> new InternalTransferEntry(internalPartySender, null, monetaryMovement))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @Test
    void shouldThrowNullPointerException_whenMonetaryMovementIsNull() {
        var internalPartySender = InternalAccountSender.of(SENDER_ACCOUNT_ID);
        var internalPartyRecipient = InternalAccountRecipient.of(RECIPIENT_ACCOUNT_ID);

        assertThatThrownBy(() -> new InternalTransferEntry(internalPartySender, internalPartyRecipient, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(TransferEntry.MONETARY_MOVEMENT_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @ArgumentsSource(MovementTypeAndCurrencyArgumentsProvider.class)
    void shouldThrowNullPointerException_whenUsingOfFactoryMethodWithSenderAccountIdIsNull(MovementType movementType, Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        assertThatThrownBy(() -> InternalTransferEntry.of(null, RECIPIENT_ACCOUNT_ID, movementType, monetaryAmount))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartySender.SENDER_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @ArgumentsSource(MovementTypeAndCurrencyArgumentsProvider.class)
    void shouldThrowNullPointerException_whenUsingOfFactoryMethodWithRecipientAccountIdIsNull(MovementType movementType, Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        assertThatThrownBy(() -> InternalTransferEntry.of(SENDER_ACCOUNT_ID, null, movementType, monetaryAmount))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenUsingOfFactoryMethodWithMovementTypeIsNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);
        assertThatThrownBy(() -> InternalTransferEntry.of(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, null, monetaryAmount))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(MonetaryMovement.MOVEMENT_TYPE_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @EnumSource(MovementType.class)
    void shouldThrowNullPointerException_whenUsingOfFactoryMethodWithMonetaryAmountIsNull(MovementType movementType) {
        assertThatThrownBy(() -> InternalTransferEntry.of(SENDER_ACCOUNT_ID, RECIPIENT_ACCOUNT_ID, movementType, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(MonetaryMovement.MONETARY_AMOUNT_NOT_NULL_MESSAGE);
    }
}
