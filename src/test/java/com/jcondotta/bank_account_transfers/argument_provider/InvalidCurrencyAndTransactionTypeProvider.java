package com.jcondotta.bank_account_transfers.argument_provider;

import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class InvalidCurrencyAndTransactionTypeProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        String[] invalidCurrencies = {"usD", "eu1", "1EU", "US9", "123", "USDX", "EURO"};

        return Stream.of(TransactionType.values())
                .flatMap(type -> Stream.of(invalidCurrencies)
                        .map(currency -> Arguments.of(type, currency)));
    }
}
