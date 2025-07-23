package com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer.model;

import com.jcondotta.bank_account_transfers.application.TestAccountDetails;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountSender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalIbanRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartyRecipient;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.PartySender;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CreateInternalTransferToAccountIbanCommandTest {

    private static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");

    private static final BankAccountId SENDER_ACCOUNT_ID = BankAccountId.newId();
    private static final Iban RECIPIENT_IBAN = Iban.of(TestAccountDetails.JEFFERSON.getIban());

    private static final InternalAccountSender INTERNAL_ACCOUNT_SENDER = InternalAccountSender.of(SENDER_ACCOUNT_ID);
    private static final InternalIbanRecipient INTERNAL_IBAN_RECIPIENT = InternalIbanRecipient.of(RECIPIENT_IBAN);

    private static final String TRANSFER_REFERENCE = "transfer reference";

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateCommand_whenRequestIsValid(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        var createInternalTransferCommand = new CreateInternalTransferToIbanCommand(
            INTERNAL_ACCOUNT_SENDER, INTERNAL_IBAN_RECIPIENT, monetaryAmount, TRANSFER_REFERENCE
        );

        assertThat(createInternalTransferCommand)
            .satisfies(command -> {
                assertThat(command.partySender().bankAccountId()).isEqualTo(SENDER_ACCOUNT_ID);
                assertThat(command.partyRecipient().iban()).isEqualTo(RECIPIENT_IBAN);
                assertThat(command.monetaryAmount()).isEqualTo(monetaryAmount);
                assertThat(command.reference()).isEqualTo(TRANSFER_REFERENCE);
            });
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenInternalAccountSenderIsNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        assertThatThrownBy(() -> new CreateInternalTransferToIbanCommand(null, INTERNAL_IBAN_RECIPIENT, monetaryAmount, TRANSFER_REFERENCE))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.BankTransfer.SENDER_ACCOUNT_ID_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenInternalIbanRecipientIsNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        assertThatThrownBy(() -> new CreateInternalTransferToIbanCommand(INTERNAL_ACCOUNT_SENDER, null, monetaryAmount, TRANSFER_REFERENCE))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.BankTransfer.RECIPIENT_IBAN_NOT_NULL);
    }

    @Test
    void shouldThrowNullPointerException_whenMonetaryAmountIsNull() {
        assertThatThrownBy(() -> new CreateInternalTransferToIbanCommand(INTERNAL_ACCOUNT_SENDER, INTERNAL_IBAN_RECIPIENT, null, TRANSFER_REFERENCE))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.BankTransfer.MONETARY_AMOUNT_NOT_NULL);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldCreateCommand_whenTransferReferenceIsNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        var createInternalTransferCommand = CreateInternalTransferToIbanCommand.of(INTERNAL_ACCOUNT_SENDER, INTERNAL_IBAN_RECIPIENT, monetaryAmount, null);
        assertThat(createInternalTransferCommand)
            .satisfies(command -> assertThat(command.reference()).isNull());
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenUsingOfFactoryMethodWithSenderAccountIdNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        assertThatThrownBy(() -> CreateInternalTransferToIbanCommand.of(null, RECIPIENT_IBAN, monetaryAmount))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartySender.SENDER_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @EnumSource(Currency.class)
    void shouldThrowNullPointerException_whenUsingOfFactoryMethodWithRecipientAccountIdNull(Currency currency) {
        var monetaryAmount = MonetaryAmount.of(AMOUNT_200, currency);

        assertThatThrownBy(() -> CreateInternalTransferToIbanCommand.of(SENDER_ACCOUNT_ID, null, monetaryAmount))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartyRecipient.RECIPIENT_IDENTIFIER_NOT_NULL_MESSAGE);
    }

    @Test
    void shouldThrowNullPointerException_whenUsingOfFactoryMethodWithMonetaryAmountNull() {
        assertThatThrownBy(() -> CreateInternalTransferToIbanCommand.of(SENDER_ACCOUNT_ID, RECIPIENT_IBAN, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.BankTransfer.MONETARY_AMOUNT_NOT_NULL);
    }
}
