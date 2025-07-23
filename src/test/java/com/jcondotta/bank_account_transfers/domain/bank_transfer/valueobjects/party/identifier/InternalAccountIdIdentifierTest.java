package com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.identifier;

import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InternalAccountIdIdentifierTest {

    private static final BankAccountId BANK_ACCOUNT_ID = BankAccountId.newId();

    @Test
    void shouldCreateInternalAccountIdIdentifier_whenBankAccountIdIsValid() {
        var identifier = InternalAccountIdIdentifier.of(BANK_ACCOUNT_ID);

        assertThat(identifier)
            .satisfies(value -> {
                assertThat(value.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID);
                assertThat(value.asString()).isEqualTo(BANK_ACCOUNT_ID.toString());
            });
    }

    @Test
    void shouldThrowNullPointerException_whenBankAccountIdIsNull() {
        assertThatThrownBy(() -> InternalAccountIdIdentifier.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(BankAccountId.ID_NOT_NULL_MESSAGE);
    }

}