package com.jcondotta.bank_account_transfers.interfaces.rest.request_internal_transfer.mapper;

import com.jcondotta.bank_account_transfers.application.usecases.request_internal_transfer.model.RequestInternalTransferFromAccountIdToIbanCommand;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import com.jcondotta.bank_account_transfers.interfaces.rest.request_internal_transfer.model.InternalBankTransferRestRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InternalBankTransferRestRequestMapper {

    default RequestInternalTransferFromAccountIdToIbanCommand toCommand(InternalBankTransferRestRequest request){
        return RequestInternalTransferFromAccountIdToIbanCommand.of(
            BankAccountId.of(request.senderAccountId()),
            Iban.of(request.recipientIban()),
            MonetaryAmount.of(request.amount(), Currency.valueOf(request.currency())),
            request.reference()
        );
    }
}