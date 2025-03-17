package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.application.services.BankTransferDTO;
import com.jcondotta.bank_account_transfers.application.usecases.CreateBankTransferUseCase;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.Valid;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping(BankTransferAPIPaths.BASE_V1_PATH)
public class CreateBankTransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankTransferController.class);
    private final CreateBankTransferUseCase createBankTransferUseCase;

    public CreateBankTransferController(CreateBankTransferUseCase createBankTransferUseCase) {
        this.createBankTransferUseCase = createBankTransferUseCase;
    }

    @PostMapping
    @Timed(value = "bankTransfers.createBankTransfer.time", description = "Time taken to create a bank transfer",
            percentiles = {0.5, 0.9, 0.99}, extraTags = "bankTransfers")
    public ResponseEntity<BankTransferDTO> createBankTransfer(
            @RequestHeader(value = "Idempotency-Key") UUID idempotencyKey,
            @Valid @RequestBody CreateBankTransferRequest createBankTransferRequest) {

        LOGGER.info("Received request to create bank transfer with Idempotency-Key: {} - Payload: {}",
                idempotencyKey, StructuredArguments.f(createBankTransferRequest));

        var bankTransferDTO = createBankTransferUseCase.createBankTransfer(idempotencyKey, createBankTransferRequest);

        LOGGER.info("Bank transfer created successfully. Idempotency-Key: {}, BankTransferId: {}",
                idempotencyKey, bankTransferDTO.bankTransferId());

        var locationUri = BankTransferAPIPaths.v1FetchBankTransferURI(bankTransferDTO.bankTransferId());
        return ResponseEntity.created(locationUri).body(bankTransferDTO);
    }
}
