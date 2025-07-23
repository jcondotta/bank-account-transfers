package com.jcondotta.bank_account_transfers.interfaces.rest.create_outgoing_external_transfer.model;

import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "DTO for creating an outgoing external bank transfer.")
public record CreateOutgoingExternalTransferRestRequest(

    @NotNull(message = DomainErrorsMessages.BankTransfer.SENDER_ACCOUNT_ID_NOT_NULL)
    @Schema(
        description = "Unique identifier of the sender's bank account.",
        requiredMode = RequiredMode.REQUIRED,
        example = "550e8400-e29b-41d4-a716-446655440000"
    )
    UUID senderAccountId,

    @NotNull
    @Schema(
        description = "Full name of the external recipient.",
        requiredMode = RequiredMode.REQUIRED,
        example = "Jefferson Condotta"
    )
    String recipientName,

    @NotNull
    @Schema(
        description = "IBAN of the external recipient (International Bank Account Number).",
        requiredMode = RequiredMode.REQUIRED,
        example = "ES9820385778983000760236"
    )
    String recipientIban,

    @NotNull(message = DomainErrorsMessages.BankTransfer.MONETARY_AMOUNT_NOT_NULL)
    @Positive(message = DomainErrorsMessages.BankTransfer.MONETARY_AMOUNT_POSITIVE)
    @Digits(integer = 12, fraction = 2, message = "bankTransfer.monetaryAmount.invalidPrecision")
    @Schema(
        description = "Amount to transfer. Maximum 12 integer digits and 2 decimal places.",
        requiredMode = RequiredMode.REQUIRED,
        example = "250.17"
    )
    BigDecimal amount,

    @NotNull(message = DomainErrorsMessages.BankTransfer.MONETARY_CURRENCY_NOT_NULL)
    @Pattern(
        regexp = "^[A-Z]{3}$",
        message = DomainErrorsMessages.BankTransfer.MONETARY_CURRENCY_INVALID_FORMAT
    )
    @Schema(
        description = "Currency code in ISO 4217 format (3 uppercase letters).",
        requiredMode = RequiredMode.REQUIRED,
        example = "EUR"
    )
    String currency,

    @Size(max = 50, message = DomainErrorsMessages.BankTransfer.REFERENCE_TOO_LONG)
    @Schema(
        description = "Optional transfer reference or description. Max 50 characters.",
        example = "Invoice 456789"
    )
    String reference
) {}
