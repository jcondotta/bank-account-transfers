package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface BankTransferAPIPaths {

    String BASE_V1_PATH = "api/v1/bank-transfers";
    String BANK_TRANSFER_API_V1_MAPPING = BASE_V1_PATH + "/{bank-transfer-id}";

    static URI v1FetchBankTransferURI(UUID bankTransferId) {
        return UriComponentsBuilder
                .fromUriString(BANK_TRANSFER_API_V1_MAPPING)
                .buildAndExpand(bankTransferId.toString())
                .toUri();
    }
}
