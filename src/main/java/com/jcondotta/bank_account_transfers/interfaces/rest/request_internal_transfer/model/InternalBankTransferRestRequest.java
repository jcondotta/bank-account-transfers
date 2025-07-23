package com.jcondotta.bank_account_transfers.interfaces.rest.request_internal_transfer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Request to initiate an internal transfer")
public record InternalBankTransferRestRequest(

    @NotNull
    @Schema(description = "Sender bank account ID", example = "bb6e7d49-2c4f-4db4-aea7-3fa820bb4997")
    UUID senderAccountId,

    @NotBlank
    @Schema(description = "Recipient IBAN", example = "DE89370400440532013000")
    String recipientIban,

    @NotNull
    @DecimalMin(value = "0.01")
    @Schema(description = "Amount to transfer", example = "100.00")
    BigDecimal amount,

    @NotBlank
    @Schema(description = "Currency code (ISO 4217)", example = "EUR")
    String currency,

    @Schema(description = "Transfer reference", example = "Payment for invoice #1234")
    String reference
) {}
