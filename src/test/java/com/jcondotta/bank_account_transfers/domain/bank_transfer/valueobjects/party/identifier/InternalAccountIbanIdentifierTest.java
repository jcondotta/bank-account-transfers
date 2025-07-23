package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier;

import com.jcondotta.bank_account_transfers.application.TestAccountDetails;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InternalAccountIbanIdentifierTest {

    private static final Iban IBAN = Iban.of(TestAccountDetails.JEFFERSON.getIban());

    @Test
    void shouldCreateInternalAccountIdIdentifier_whenBankAccountIdIsValid() {
        var identifier = InternalAccountIbanIdentifier.of(IBAN);

        assertThat(identifier)
            .satisfies(value -> {
                assertThat(value.iban()).isEqualTo(IBAN);
                assertThat(value.asString()).isEqualTo(IBAN.toString());
            });
    }

    @Test
    void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
        assertThatThrownBy(() -> InternalAccountIdIdentifier.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(BankAccountId.ID_NOT_NULL_MESSAGE);
    }

}