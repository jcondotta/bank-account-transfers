package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.mapper;

import com.jcondotta.bank_account_transfers.domain.monetary_movement.enums.MovementType;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.persistence.enums.MovementTypeEntity;

public final class MovementTypeEntityMapper {

    private MovementTypeEntityMapper() {}

    public static MovementTypeEntity toEntity(MovementType domain) {
        return switch (domain) {
            case DEBIT -> MovementTypeEntity.DEBIT;
            case CREDIT -> MovementTypeEntity.CREDIT;
        };
    }

    public static MovementType toDomain(MovementTypeEntity entity) {
        return switch (entity) {
            case DEBIT -> MovementType.DEBIT;
            case CREDIT -> MovementType.CREDIT;
        };
    }
}
