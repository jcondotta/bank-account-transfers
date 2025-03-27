package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode;


@Schema(name = "AccountHolderDTO", description = "Represents the details of an account holder.")
public record AccountHolderDTO (

    @NotNull
    @Schema(description = "Unique identifier for the account holder.", example = "114308a6-4903-4bfb-93a8-10ebebe23524",
            requiredMode = RequiredMode.REQUIRED)
    UUID accountHolderId,

    @NotBlank
    @Schema(description = "Name of the account holder.", example = "Jefferson Condotta", requiredMode = RequiredMode.REQUIRED
    )
    String accountHolderName,

    @NotNull
    @Schema(description = "Type of the account holder (e.g., PRIMARY, JOINT).", example = "PRIMARY", requiredMode = RequiredMode.REQUIRED)
    String accountHolderType
)
{
    public AccountHolderDTO(UUID accountHolderId, String accountHolderName, String accountHolderType) {
        this.accountHolderId = accountHolderId;
        this.accountHolderName = accountHolderName;
        this.accountHolderType = accountHolderType;
    }
}