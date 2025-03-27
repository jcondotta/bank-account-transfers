package com.jcondotta.bank_account_transfers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts.BankAccountDTO;

import java.util.List;

public enum TestBankAccountDTO {

    JEFFERSON(
            new BankAccountDTO(
                    TestBankAccount.JEFFERSON.getBankAccountId(),
                    TestBankAccount.JEFFERSON.getIban(),
                    List.of())

    ),

    PATRIZIO(
            new BankAccountDTO(
                    TestBankAccount.PATRIZIO.getBankAccountId(),
                    TestBankAccount.PATRIZIO.getIban(),
                    List.of())
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

    public String getJson() {
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
