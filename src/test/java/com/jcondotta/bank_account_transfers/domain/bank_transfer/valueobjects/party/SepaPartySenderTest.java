package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party;

import com.jcondotta.bank_account_transfers.application.TestAccountDetails;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SepaPartySenderTest {

    private static final Iban IBAN_JEFFERSON = Iban.of(TestAccountDetails.JEFFERSON.getIban());
    private static final PartyName PARTY_NAME_JEFFERSON = PartyName.of(TestAccountDetails.JEFFERSON.getName());

    @Test
    void shouldCreateSepaSenderParty_whenSenderIbanAndNameAreValid() {
        assertThat(new SepaPartySender(IBAN_JEFFERSON, PARTY_NAME_JEFFERSON))
            .satisfies(senderParty -> {
                assertThat(senderParty.iban()).isEqualTo(IBAN_JEFFERSON);
                assertThat(senderParty.name()).isEqualTo(PARTY_NAME_JEFFERSON);
            });
    }

    @Test
    void shouldCreateSepaSenderParty_whenUsingOfFactoryMethod() {
        assertThat(SepaPartySender.of(IBAN_JEFFERSON, PARTY_NAME_JEFFERSON))
            .satisfies(senderParty -> {
                assertThat(senderParty.iban()).isEqualTo(IBAN_JEFFERSON);
                assertThat(senderParty.name()).isEqualTo(PARTY_NAME_JEFFERSON);
            });
    }

    @Test
    void shouldCreateSepaSenderParty_whenUsingOfFactoryMethodRawValues() {
        var iban = IBAN_JEFFERSON.value();
        var partyName = PARTY_NAME_JEFFERSON.value();

        assertThat(SepaPartySender.of(iban, partyName))
            .satisfies(senderParty -> {
                assertThat(senderParty.iban()).isEqualTo(IBAN_JEFFERSON);
                assertThat(senderParty.name()).isEqualTo(PARTY_NAME_JEFFERSON);
            });
    }

    @Test
    void shouldThrowNullPointerException_whenSenderIbanIsNull() {
        assertThatThrownBy(() -> new SepaPartySender(null, PARTY_NAME_JEFFERSON))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.SepaParty.IBAN_NOT_NULL);
    }

    @Test
    void shouldThrowNullPointerException_whenSenderNameIsNull() {
        assertThatThrownBy(() -> new SepaPartySender(IBAN_JEFFERSON, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(DomainErrorsMessages.SepaParty.NAME_NOT_NULL);
    }

    @Test
    void shouldThrowNullPointerException_whenStringSenderIbanIsNull() {
        var partyName = PARTY_NAME_JEFFERSON.value();
        assertThatThrownBy(() -> SepaPartySender.of(null, partyName))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(Iban.IBAN_VALUE_NOT_NULL_MESSAGE);
    }

    @Test
    void shouldThrowNullPointerException_whenStringSenderNameIsNull() {
        var iban = IBAN_JEFFERSON.value();
        assertThatThrownBy(() -> SepaPartySender.of(iban, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(PartyName.NAME_NOT_NULL_MESSAGE);
    }
}