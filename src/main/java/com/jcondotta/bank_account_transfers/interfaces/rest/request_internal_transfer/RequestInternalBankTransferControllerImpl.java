package com.jcondotta.bank_account_transfers.interfaces.rest.request_internal_transfer;

import com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.RequestInternalTransferUseCase;
import com.jcondotta.bank_account_transfers.interfaces.rest.request_internal_transfer.mapper.InternalBankTransferRestRequestMapper;
import com.jcondotta.bank_account_transfers.interfaces.rest.request_internal_transfer.model.InternalBankTransferRestRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RequestInternalBankTransferControllerImpl implements RequestInternalBankTransferController {

    private final RequestInternalTransferUseCase useCase;
    private final InternalBankTransferRestRequestMapper requestMapper;

    @Override
    public ResponseEntity<Void> requestInternalBankTransfer(InternalBankTransferRestRequest request) {
        useCase.execute(requestMapper.toCommand(request));
        return ResponseEntity.accepted().build();
    }
}
