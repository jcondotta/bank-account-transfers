package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.application.TestAccountDetails;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SepaRecipientPartyTest {

    private static final Iban IBAN_JEFFERSON = Iban.of(TestAccountDetails.JEFFERSON.getIban());
    private static final PartyName PARTY_NAME_JEFFERSON = PartyName.of(TestAccountDetails.JEFFERSON.getName());

    @Test
    void shouldCreateSepaRecipientParty_whenRecipientIbanAndNameAreValid() {
        assertThat(new SepaPartyRecipient(IBAN_JEFFERSON, PARTY_NAME_JEFFERSON))
            .satisfies(recipientParty -> {
                assertThat(recipientParty.iban()).isEqualTo(IBAN_JEFFERSON);
                assertThat(recipientParty.name()).isEqualTo(PARTY_NAME_JEFFERSON);
            });
    }

    @Test
    void shouldCreateSepaRecipientParty_whenUsingOfFactoryMethod() {
        assertThat(SepaPartyRecipient.of(IBAN_JEFFERSON, PARTY_NAME_JEFFERSON))
            .satisfies(recipientParty -> {
                assertThat(recipientParty.iban()).isEqualTo(IBAN_JEFFERSON);
                assertThat(recipientParty.name()).isEqualTo(PARTY_NAME_JEFFERSON);
            });
    }

    @Test
    void shouldCreateSepaRecipientParty_whenUsingOfFactoryMethodRawValues() {
        var iban = IBAN_JEFFERSON.value();
        var partyName = PARTY_NAME_JEFFERSON.value();

        assertThat(SepaPartyRecipient.of(iban, partyName))
            .satisfies(recipientParty -> {
                assertThat(recipientParty.iban()).isEqualTo(IBAN_JEFFERSON);
                assertThat(recipientParty.name()).isEqualTo(PARTY_NAME_JEFFERSON);
            });
    }

    @Test
    void shouldThrowNullPointerException_whenRecipientIbanIsNull() {
        assertThatThrownBy(() -> new SepaPartyRecipient(null, PARTY_NAME_JEFFERSON))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.SepaParty.IBAN_NOT_NULL);
    }

    @Test
    void shouldThrowNullPointerException_whenRecipientNameIsNull() {
        assertThatThrownBy(() -> new SepaPartyRecipient(IBAN_JEFFERSON, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.SepaParty.NAME_NOT_NULL);
    }

    @Test
    void shouldThrowNullPointerException_whenStringRecipientIbanIsNull() {
        var partyName = PARTY_NAME_JEFFERSON.value();
        assertThatThrownBy(() -> SepaPartyRecipient.of(null, partyName))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(Iban.IBAN_VALUE_NOT_NULL_MESSAGE);
    }

    @Test
    void shouldThrowNullPointerException_whenStringRecipientNameIsNull() {
        var iban = IBAN_JEFFERSON.value();
        assertThatThrownBy(() -> SepaPartyRecipient.of(iban, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartyName.NAME_NOT_NULL_MESSAGE);
    }
}