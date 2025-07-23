package com.jcondotta.bank_account_transfers.interfaces.kinesis.request_internal_transfer.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record RequestInternalTransferEvent(
    UUID senderAccountId,
    String recipientIban,
    BigDecimal amount,
    String currency,
    String reference,
    ZonedDateTime requestedAt)
{ }
