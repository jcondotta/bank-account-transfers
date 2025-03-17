package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import java.util.List;

public record FieldValidationError(String field, List<String> messages) {}
