package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.mapper;

import com.jcondotta.bank_account_transfers.domain.bank_transfer.enums.TransferType;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.enums.TransferTypeEntity;

public final class TransferTypeEntityMapper {

    private TransferTypeEntityMapper() {}

    public static TransferTypeEntity toEntity(TransferType domain) {
        return switch (domain) {
            case INTERNAL -> TransferTypeEntity.INTERNAL;
            case OUTGOING_EXTERNAL -> TransferTypeEntity.OUTGOING_EXTERNAL;
            case INCOMING_EXTERNAL -> TransferTypeEntity.INCOMING_EXTERNAL;
        };
    }

    public static TransferType toDomain(TransferTypeEntity entity) {
        return switch (entity) {
            case INTERNAL -> TransferType.INTERNAL;
            case OUTGOING_EXTERNAL -> TransferType.OUTGOING_EXTERNAL;
            case INCOMING_EXTERNAL -> TransferType.INCOMING_EXTERNAL;
        };
    }
}
