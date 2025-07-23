package com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer.model.CreateInternalBankTransferRestRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
public class CreateInternalBankTransferControllerImpl implements CreateInternalBankTransferController{

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInternalBankTransferControllerImpl.class);

    @Override
    public ResponseEntity<Void> createBankAccount(CreateInternalBankTransferRestRequest request) {
        LOGGER.atInfo()
            .setMessage("Received request to execute a bank transfer between bank account(sender): {} and bank account(recipient): {}")
            .addArgument(request.senderAccountId())
            .addArgument(request.recipientIban())
            .log();

        return ResponseEntity.accepted().build();
    }
}