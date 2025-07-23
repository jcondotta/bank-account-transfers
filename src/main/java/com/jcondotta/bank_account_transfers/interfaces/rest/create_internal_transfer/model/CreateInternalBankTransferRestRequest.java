package com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer.model;

import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "DTO for handling internal bank transfers.")
public record CreateInternalBankTransferRestRequest(

    @NotNull(message = DomainErrorsMessages.BankTransfer.SENDER_ACCOUNT_ID_NOT_NULL)
    @Schema(description = "Sender's account ID", requiredMode = RequiredMode.REQUIRED, example = "550e8400-e29b-41d4-a716-446655440000")
    UUID senderAccountId,

    @NotNull(message = DomainErrorsMessages.BankTransfer.RECIPIENT_IBAN_NOT_NULL)
    @Schema(description = "Recipient's iban", requiredMode = RequiredMode.REQUIRED, example = "GI85PJZEGj1HPvBgCp5vzfE")
    String recipientIban,

    @NotNull(message = DomainErrorsMessages.BankTransfer.MONETARY_AMOUNT_NOT_NULL)
    @Positive(message = DomainErrorsMessages.BankTransfer.MONETARY_AMOUNT_POSITIVE)
    @Digits(integer = 12, fraction = 2, message = "bankTransfer.monetaryAmount.invalidPrecision")
    @Schema(description = "Amount of transfer (max 12 digits, 2 decimal places)", requiredMode = RequiredMode.REQUIRED, example = "250.17")
    BigDecimal amount,

    @NotNull(message = DomainErrorsMessages.BankTransfer.MONETARY_CURRENCY_NOT_NULL)
    @Pattern(regexp = "^[A-Z]{3}$", message = DomainErrorsMessages.BankTransfer.MONETARY_CURRENCY_INVALID_FORMAT)
    @Schema(description = "Currency (ISO 4217, 3-letter code)", requiredMode = RequiredMode.REQUIRED, examples = {"EUR", "USD", "GBP"})
    String currency,

    @Size(max = 50, message = DomainErrorsMessages.BankTransfer.REFERENCE_TOO_LONG)
    @Schema(description = "transfer reference", example = "Invoice 456789")
    String reference
) {}