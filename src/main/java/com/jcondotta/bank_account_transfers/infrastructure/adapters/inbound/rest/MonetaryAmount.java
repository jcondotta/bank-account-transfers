package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import java.math.BigDecimal;

public interface MonetaryAmount {
    BigDecimal amount();
    String currency();
}