package com.jcondotta.bank_account_transfers.interfaces.rest.create_outgoing_external_transfer;

import com.jcondotta.bank_account_transfers.interfaces.rest.create_outgoing_external_transfer.model.CreateOutgoingExternalTransferRestRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
public class CreateOutgoingExternalTransferControllerImpl implements CreateOutgoingExternalTransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateOutgoingExternalTransferControllerImpl.class);

//    private final CreateOutgoingExternalTransferUseCase useCase;
//    private final CreateOutgoingExternalTransferRestRequestMapper restRequestMapper;

    @Override
    public ResponseEntity<Void> createBankAccount(@Valid CreateOutgoingExternalTransferRestRequest request) {
//        LOGGER.atInfo()
//            .setMessage("Received request to execute a bank transfer between bank account(sender): {} and bank account(recipient): {}")
//            .addArgument(request.partySender())
//            .addArgument(request.partyRecipient())
//            .log();

//        useCase.execute(restRequestMapper.toCommand(request));

        return ResponseEntity.accepted().build();
    }
}