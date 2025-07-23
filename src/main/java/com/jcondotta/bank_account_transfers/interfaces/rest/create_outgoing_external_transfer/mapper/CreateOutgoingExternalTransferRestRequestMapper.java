package com.jcondotta.bank_account_transfers.interfaces.rest.create_outgoing_external_transfer.mapper;

import com.jcondotta.bank_account_transfers.application.usecases.create_outgoing_external_transfer.model.CreateOutgoingExternalTransferCommandImpl;
import com.jcondotta.bank_account_transfers.interfaces.rest.create_outgoing_external_transfer.model.CreateOutgoingExternalTransferRestRequest;

//@Mapper(componentModel = "spring", imports = { BankAccountId.class, PartyName.class, Iban.class})
public interface CreateOutgoingExternalTransferRestRequestMapper {

//    @Mapping(target = "partySender", expression = "java(BankAccountId.of(request.partySender()))")
//    @Mapping(target = "recipientName", expression = "java(PartyName.of(request.recipientName()))")
//    @Mapping(target = "recipientIban", expression = "java(Iban.of(request.recipientIban()))")
//    @Mapping(target = "monetaryAmount", source = "request", qualifiedByName = "mapMonetaryAmount")
    CreateOutgoingExternalTransferCommandImpl toCommand(CreateOutgoingExternalTransferRestRequest request);

//    @Named("mapMonetaryAmount")
//    static MonetaryAmount mapMonetaryAmount(CreateOutgoingExternalTransferRestRequest request) {
//        return MonetaryAmount.of(request.amount(), Currency.valueOf(request.currency()));
//    }
}