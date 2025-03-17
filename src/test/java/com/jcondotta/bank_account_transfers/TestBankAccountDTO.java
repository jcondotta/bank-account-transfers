package com.jcondotta.bank_account_transfers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.BankAccountDTO;

public enum TestBankAccountDTO {

    JEFFERSON(
            new BankAccountDTO(
                    TestBankAccount.JEFFERSON.getBankAccountId(),
                    TestBankAccount.JEFFERSON.getIban())
    ),

    PATRIZIO(
            new BankAccountDTO(
                    TestBankAccount.PATRIZIO.getBankAccountId(),
                    TestBankAccount.PATRIZIO.getIban())
    );

    private final BankAccountDTO bankAccountDTO;
    private final String jsonBankAccountDTO;

    TestBankAccountDTO(BankAccountDTO bankAccountDTO) {
        this.bankAccountDTO = bankAccountDTO;
        this.jsonBankAccountDTO = convertToJson(bankAccountDTO);
    }

    public BankAccountDTO getBankAccountDTO() {
        return bankAccountDTO;
    }

    public String getJsonBankAccountDTO() {
        return jsonBankAccountDTO;
    }

    // ✅ Helper method to convert DTO to JSON
    private static String convertToJson(BankAccountDTO dto) {
        try {
            return new ObjectMapper().writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing BankAccountDTO", e);
        }
    }
}
