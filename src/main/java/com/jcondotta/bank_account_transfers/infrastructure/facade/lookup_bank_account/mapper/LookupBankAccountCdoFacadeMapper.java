package com.jcondotta.bank_account_transfers.infrastructure.facade.lookup_bank_account.mapper;

import com.jcondotta.bank_account_transfers.domain.bank_account.enums.AccountStatus;
import com.jcondotta.bank_account_transfers.domain.bank_account.enums.AccountType;
import com.jcondotta.bank_account_transfers.domain.bank_account.model.BankAccount;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.client.lookup_bank_account.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.ZonedDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface LookupBankAccountCdoFacadeMapper {

    LookupBankAccountCdoFacadeMapper INSTANCE = Mappers.getMapper(LookupBankAccountCdoFacadeMapper.class);

    @Mapping(target = "bankAccountId", source = "bankAccountId")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "iban", source = "iban")
    @Mapping(target = "createdAt", source = "dateOfOpening")
    BankAccount map(BankAccountCdo bankAccountCdo);

    default BankAccountId map(UUID bankAccountId) {
        return BankAccountId.of(bankAccountId);
    }

    default AccountType map(AccountTypeCdo cdo) {
        return AccountType.valueOf(cdo.name());
    }

    default Currency map(CurrencyCdo cdo) {
        return Currency.valueOf(cdo.name());
    }

    default AccountStatus map(AccountStatusCdo cdo) {
        return AccountStatus.valueOf(cdo.name());
    }

    default Iban map(String value) {
        return Iban.of(value);
    }

    default OccurredAt map(ZonedDateTime dateTime) {
        return OccurredAt.of(dateTime);
    }
}
