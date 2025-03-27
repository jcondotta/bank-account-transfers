package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts;

import java.util.List;
import java.util.UUID;

public record BankAccountDTO(UUID bankAccountId, String iban, List<AccountHolderDTO> accountHolders){}
