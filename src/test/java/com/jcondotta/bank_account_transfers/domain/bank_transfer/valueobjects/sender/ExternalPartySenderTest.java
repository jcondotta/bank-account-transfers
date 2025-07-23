package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.sender;

class ExternalPartySenderTest {

//    private static final String RECIPIENT_NAME = "Banco Inter SA";
//    private static final String RECIPIENT_IBAN = "ES9820385778983000760236";
//
//    @Test
//    void shouldCreateExternalPartyRecipient_whenRecipientNameAndIbanAreValid() {
//        var expectedName = PartyName.of(RECIPIENT_NAME);
//        var expectedIban = Iban.of(RECIPIENT_IBAN);
//
//        assertThat(ExternalPartySender3.of(expectedName, expectedIban))
//            .satisfies(recipient -> {
//                assertThat(recipient.senderName()).isEqualTo(expectedName);
//                assertThat(recipient.senderIban()).isEqualTo(expectedIban);
//            });
//    }
//
//    @Test
//    void shouldCreateExternalPartyRecipient_whenUsingStringFactoryMethod() {
//        assertThat(ExternalPartySender3.of(RECIPIENT_NAME, RECIPIENT_IBAN))
//            .satisfies(recipient -> {
//                assertThat(recipient.senderName().value()).isEqualTo(RECIPIENT_NAME);
//                assertThat(recipient.senderIban().value()).isEqualTo(RECIPIENT_IBAN);
//            });
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenRecipientNameIsNull() {
//        var iban = Iban.of(RECIPIENT_IBAN);
//
//        assertThatThrownBy(() -> ExternalPartySender3.of(null, iban))
//            .isInstanceOf(NullPointerException.class)
//            .hasMessage(DomainErrorsMessages.ExternalPartySender.PARTY_NAME_NOT_NULL);
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenRecipientIbanIsNull() {
//        var name = PartyName.of(RECIPIENT_NAME);
//
//        assertThatThrownBy(() -> ExternalPartySender3.of(name, null))
//            .isInstanceOf(NullPointerException.class)
//            .hasMessage(DomainErrorsMessages.ExternalPartySender.IBAN_NOT_NULL);
//    }

}