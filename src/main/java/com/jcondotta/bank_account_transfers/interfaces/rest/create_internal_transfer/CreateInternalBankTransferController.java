package com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer;

import com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer.model.CreateInternalBankTransferRestRequest;
import com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer.model.CreateInternalBankTransferRestRequest2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/v1/bank-transfers")
public interface CreateInternalBankTransferController {

    @Operation(summary = "Initiate internal transfer between bank accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "The internal bank transfer request was accepted and is being processed."),
        @ApiResponse(responseCode = "400", description = "Invalid request. One or more fields are missing or contain invalid values."),
        @ApiResponse(responseCode = "500", description = "Unexpected error occurred while processing the transfer request.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<Void> createBankAccount(@Valid @RequestBody CreateInternalBankTransferRestRequest request);
}