package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.*;

@Schema(description = "Universal DTO for handling bank transfers (internal or external).")
public record CreateBankTransferRequest(

    @NotNull(message = "transfer.senderBankAccountId.notNull")
    @Schema(description = "Sender's bank account ID", requiredMode = RequiredMode.REQUIRED, example = "550e8400-e29b-41d4-a716-446655440000")
    UUID senderBankAccountId,

    @NotNull(message = "transfer.recipientIban.notNull")
    @Size(min = 15, max = 34, message = "transfer.recipientIban.size")
    @Schema(description = "Recipient IBAN (15-34 characters, follows IBAN format)", requiredMode = RequiredMode.REQUIRED, example = "ES3801283316232166447417")
    String recipientIban,

    @NotNull(message = "transfer.recipientName.notNull")
    @Size(min = 5, max = 100, message = "transfer.recipientName.size")
    @Schema(description = "Recipient full name (5-100 characters)", requiredMode = RequiredMode.REQUIRED, example = "Jefferson Condotta")
    String recipientName,

    @NotNull(message = "transfer.amount.notNull")
    @Positive(message = "transfer.amount.positive")
    @Digits(integer = 12, fraction = 2, message = "transfer.amount.invalidPrecision")
    @Schema(description = "Amount of transfer (max 12 digits, 2 decimal places)", requiredMode = RequiredMode.REQUIRED, example = "250.00")
    BigDecimal amount,

    @NotNull(message = "transfer.currency.notNull")
    @Pattern(regexp = "^[A-Z]{3}$", message = "transfer.currency.invalidFormat")
    @Schema(description = "Currency (ISO 4217, 3-letter code)", requiredMode = RequiredMode.REQUIRED, example = "EUR")
    String currency,

    @Size(max = 50, message = "transfer.reference.tooLong")
    @Schema(description = "Optional transfer reference", example = "Invoice 456789")
    String reference

) {}