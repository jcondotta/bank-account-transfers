package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.application.ports.outbound.persistence.BankTransferRepositoryPort;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(BankTransferAPIPaths.BANK_TRANSFER_API_V1_MAPPING)
public class FindBankTransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindBankTransferController.class);
    private final BankTransferRepositoryPort bankTransferRepositoryPort;

    public FindBankTransferController(BankTransferRepositoryPort bankTransferRepositoryPort) {
        this.bankTransferRepositoryPort = bankTransferRepositoryPort;
    }

    @GetMapping
    @Timed(value = "bankTransfers.findBankTransfer.time", description = "Time taken to find a bank transfer",
            percentiles = {0.5, 0.9, 0.99}, extraTags = "bankTransfers")
    public ResponseEntity<Void> findBankTransfer(@PathVariable("bank-transfer-id") UUID bankTransferId) {
//        LOGGER.info("Received request to create bank transfer with Idempotency-Key: {} - Payload: {}",
//                idempotencyKey, StructuredArguments.f(createBankTransferRequest));

//        List<BankTransfer> all = bankTransferRepositoryPort.findAll();
//        System.out.println(all);
//        Optional<BankTransfer> byId = bankTransferRepositoryPort.findById(bankTransferId);

//        BankTransferDTO bankTransferDTO = byId.map(BankTransferDTO::new).orElseThrow();
        return ResponseEntity.ok().build();
    }
}
