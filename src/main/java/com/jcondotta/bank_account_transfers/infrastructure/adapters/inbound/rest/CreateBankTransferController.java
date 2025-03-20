package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.application.usecases.CreateBankTransferUseCase;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(BankTransferAPIPaths.BASE_V1_PATH)
@Tag(name = "Bank Transfers", description = "Endpoint for creating bank transfers.")
public class CreateBankTransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankTransferController.class);

    private final CreateBankTransferUseCase createBankTransferUseCase;
    private final Timer createBankTransferTimer;

    public CreateBankTransferController(CreateBankTransferUseCase createBankTransferUseCase,
                                        MeterRegistry meterRegistry) {
        this.createBankTransferUseCase = createBankTransferUseCase;
        this.createBankTransferTimer = meterRegistry.timer("bankTransfers.createBankTransfer3.time");
    }

    @PostMapping
    @Timed(value = "bankTransfers.createBankTransfer.time", description = "Time taken to create a bank transfer",
            percentiles = {0.5, 0.9, 0.99}, extraTags = "bankTransfers")
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
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid request data or missing required fields",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = String.class),
//                            examples = {
//                                    @ExampleObject(
//                                            name = "ValidationErrorExample",
//                                            value = """
//                {
//                  "status": 400,
//                  "title": "Bad Request",
//                  "timestamp": "2025-03-18T14:00:00Z",
//                  "path": "api/v1/bank-transfers",
//                  "errors": [
//                    {
//                      "field": "amount",
//                      "messages": ["must be greater than zero"]
//                    },
//                    {
//                      "field": "recipientIban",
//                      "messages": ["must not be null"]
//                    }
//                  ]
//                }
//                """
//                                    )
//                            }
//                    )
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error. This may occur due to system issues or unexpected errors during processing.",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = String.class)
//                    )
//            )
    })
    public ResponseEntity<BankTransferDTO> createBankTransfer(
            @Parameter(
                    description = "Unique idempotency key to ensure the request is only processed once",
                    required = true, example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @RequestHeader(value = "Idempotency-Key") UUID idempotencyKey,
            @RequestBody(
                    description = "Request payload containing sender account, recipient details, amount, and other data.",
                    required = true
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody
            CreateBankTransferRequest createBankTransferRequest) {

        long startTime = System.nanoTime();

//        LOGGER.atInfo().setMessage("Received request to create bank transfer with Idempotency-Key: {} - Payload: {}")
//                .addArgument(idempotencyKey)
//                .addArgument(createBankTransferRequest)
//                .addaddKeyValue("userId", "1").log();
//        LOGGER.info("Received request to create bank transfer with Idempotency-Key: {} - Payload: {}",
//                idempotencyKey, StructuredArguments.f(createBankTransferRequest));

        var bankTransferDTO = createBankTransferUseCase.createBankTransfer(idempotencyKey, createBankTransferRequest);
        createBankTransferTimer.record(System.nanoTime() - startTime, java.util.concurrent.TimeUnit.NANOSECONDS);

        LOGGER.info("Bank transfer created successfully. Idempotency-Key: {}, BankTransferId: {}",
                idempotencyKey, bankTransferDTO.bankTransferId());

        var locationUri = BankTransferAPIPaths.v1FetchBankTransferURI(bankTransferDTO.bankTransferId());
        return ResponseEntity.created(locationUri).body(bankTransferDTO);
    }
}
