package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.application.usecases.CreateBankTransferUseCase;
import com.jcondotta.bank_account_transfers.application.usecases.DoubleEntryTransactionDTO;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("${api.v1.bank-transfers.root-path}")
@Tag(name = "Bank Transfers", description = "Endpoint for creating bank transfers.")
public class CreateBankTransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankTransferController.class);

    private final CreateBankTransferUseCase createBankTransferUseCase;

    public CreateBankTransferController(CreateBankTransferUseCase createBankTransferUseCase) {
        this.createBankTransferUseCase = createBankTransferUseCase;
    }

    @PostMapping
    @Timed(value = "bankTransfers.createBankTransfer.time", description = "Time taken to create a bank transfer", percentiles = {0.5, 0.9, 0.95, 0.99})
    @Operation(summary = "Create a new bank transfer", description = "Creates a new bank transfer using the given request payload and a unique Idempotency-Key.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Bank transfer created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BankTransferDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or missing required fields",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(
                                            name = "ValidationErrorExample",
                                            value = """
                                            {
                                              "status": 400,
                                              "title": "Bad Request",
                                              "timestamp": "2025-03-18T14:00:00Z",
                                              "path": "api/v1/bank-transfers",
                                              "errors": [
                                                {
                                                  "field": "amount",
                                                  "messages": ["must be greater than zero"]
                                                },
                                                {
                                                  "field": "recipientIban",
                                                  "messages": ["must not be null"]
                                                }
                                              ]
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error. This may occur due to system issues or unexpected errors during processing.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    public ResponseEntity<DoubleEntryTransactionDTO> createBankTransfer(@Valid @RequestBody CreateBankTransferRequest request) {
        LOGGER.info("Received request to create bank transfer", StructuredArguments.f(request));

        var transactionDTO = createBankTransferUseCase.createBankTransfer(request);

//        LOGGER.info("Bank transfer created successfully. TransactionID: {}", transactionDTO.transactionId());

        var locationUri = BankTransferAPIPaths.v1FetchBankTransferURI(UUID.randomUUID());
        return ResponseEntity.created(locationUri).body(transactionDTO);
    }
}
