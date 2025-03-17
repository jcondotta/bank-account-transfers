package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import java.util.UUID;

public record BankAccountDTO(UUID bankAccountId, String iban){}
