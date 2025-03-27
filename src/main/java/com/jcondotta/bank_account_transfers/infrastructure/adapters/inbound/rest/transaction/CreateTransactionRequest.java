package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.transaction;

import com.jcondotta.bank_account_transfers.domain.models.monetary.MonetaryMovement;
import com.jcondotta.bank_account_transfers.domain.validation.Validatable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTransactionRequest(
        @NotNull(message = "transaction.bankAccountId.notNull")
        UUID bankAccountId,

        @NotNull(message = "transaction.monetaryMovement.notNull")
        MonetaryMovement monetaryMovement,

        @Size(max = 50, message = "transaction.reference.tooLong")
        String reference
) implements Validatable {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID bankAccountId;
        private MonetaryMovement monetaryMovement;
        private String reference;

        public Builder bankAccountId(UUID bankAccountId) {
            this.bankAccountId = bankAccountId;
            return this;
        }

        public Builder monetaryMovement(MonetaryMovement monetaryMovement) {
            this.monetaryMovement = monetaryMovement;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public CreateTransactionRequest build() {
            return new CreateTransactionRequest(bankAccountId, monetaryMovement, reference);
        }
    }
}
