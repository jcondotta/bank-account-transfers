package com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer.mapper;

import com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer.model.CreateInternalTransferToIbanCommand;
import com.jcondotta.bank_account_transfers.domain.bank_account.valueobject.BankAccountId;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.iban.Iban;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalAccountSender;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.valueobjects.party.InternalIbanRecipient;
import com.jcondotta.bank_account_transfers.domain.monetary_movement.value_objects.MonetaryAmount;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import com.jcondotta.bank_account_transfers.interfaces.rest.create_internal_transfer.model.CreateInternalBankTransferRestRequest2;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateInternalBankTransferRestRequestMapper {

    default CreateInternalTransferToIbanCommand toCommand(CreateInternalBankTransferRestRequest2 request){
        return CreateInternalTransferToIbanCommand.of(
            InternalAccountSender.of(BankAccountId.of(request.senderAccountId())),
            InternalIbanRecipient.of(Iban.of(request.recipientIban())),
            MonetaryAmount.of(request.amount(), Currency.valueOf(request.currency())),
            request.reference()
        );
    }
}