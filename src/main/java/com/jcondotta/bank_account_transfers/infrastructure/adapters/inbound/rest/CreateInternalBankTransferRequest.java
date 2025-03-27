package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.domain.validation.Validatable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "DTO for handling internal bank transfers.")
public record CreateInternalBankTransferRequest(

    @NotNull(message = "transfer.senderBankAccountId.notNull")
    @Schema(description = "Sender's bank account ID", requiredMode = RequiredMode.REQUIRED, example = "550e8400-e29b-41d4-a716-446655440000")
    UUID senderBankAccountId,

    @NotNull(message = "transfer.recipientBankAccountId.notNull")
    @Schema(description = "Recipient's bank account ID", requiredMode = RequiredMode.REQUIRED, example = "99d86af1-13b1-4e87-87c7-086613d38552")
    String recipientBankAccountId,

    @NotNull(message = "transfer.amount.notNull")
    @Positive(message = "transfer.amount.positive")
    @Digits(integer = 12, fraction = 2, message = "transfer.amount.invalidPrecision")
    @Schema(description = "Amount of transfer (max 12 digits, 2 decimal places)", requiredMode = RequiredMode.REQUIRED, example = "250.17")
    BigDecimal amount,

    @NotNull(message = "transfer.currency.notNull")
    @Pattern(regexp = "^[A-Z]{3}$", message = "transfer.currency.invalidFormat")
    @Schema(description = "Currency (ISO 4217, 3-letter code)", requiredMode = RequiredMode.REQUIRED, example = "EUR")
    String currency,

    @Size(max = 50, message = "transfer.reference.tooLong")
    @Schema(description = "transfer reference", example = "Invoice 456789")
    String reference

) implements Validatable {}