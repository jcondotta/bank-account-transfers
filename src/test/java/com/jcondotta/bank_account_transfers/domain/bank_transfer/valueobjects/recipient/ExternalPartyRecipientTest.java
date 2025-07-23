package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.recipient;

class ExternalPartyRecipientTest {

//    private static final String RECIPIENT_NAME = "Banco Inter SA";
//    private static final String RECIPIENT_IBAN = "ES9820385778983000760236";
//
//    @Test
//    void shouldCreateExternalPartyRecipient_whenRecipientNameAndIbanAreValid() {
//        var expectedName = PartyName.of(RECIPIENT_NAME);
//        var expectedIban = Iban.of(RECIPIENT_IBAN);
//
//        assertThat(SEPARecipient.of(expectedName, expectedIban))
//            .satisfies(recipient -> {
//                assertThat(recipient.recipientName()).isEqualTo(expectedName);
//                assertThat(recipient.recipientIban()).isEqualTo(expectedIban);
//            });
//    }
//
//    @Test
//    void shouldCreateExternalPartyRecipient_whenUsingStringFactoryMethod() {
//        assertThat(SEPARecipient.of(RECIPIENT_NAME, RECIPIENT_IBAN))
//            .satisfies(recipient -> {
//                assertThat(recipient.recipientName().value()).isEqualTo(RECIPIENT_NAME);
//                assertThat(recipient.recipientIban().value()).isEqualTo(RECIPIENT_IBAN);
//            });
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenRecipientNameIsNull() {
//        var iban = Iban.of(RECIPIENT_IBAN);
//
//        assertThatThrownBy(() -> SEPARecipient.of(null, iban))
//            .isInstanceOf(NullPointerException.class)
//            .hasMessage(DomainErrorsMessages.ExternalPartyRecipient.PARTY_NAME_NOT_NULL);
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenRecipientIbanIsNull() {
//        var name = PartyName.of(RECIPIENT_NAME);
//
//        assertThatThrownBy(() -> SEPARecipient.of(name, null))
//            .isInstanceOf(NullPointerException.class)
//            .hasMessage(DomainErrorsMessages.ExternalPartyRecipient.IBAN_NOT_NULL);
//    }
}
