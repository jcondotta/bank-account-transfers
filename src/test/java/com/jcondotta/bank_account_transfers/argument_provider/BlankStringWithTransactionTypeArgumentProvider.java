package com.jcondotta.bank_account_transfers.argument_provider;

import com.jcondotta.bank_account_transfers.domain.models.TransactionType;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class BlankStringWithTransactionTypeArgumentProvider implements ArgumentsProvider {

    private final BlankValuesArgumentProvider blankProvider = new BlankValuesArgumentProvider();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Stream<? extends Arguments> blankValues = blankProvider.provideArguments(context);

        return blankValues.flatMap(blankArg -> {
            return Stream.of(TransactionType.values())
                    .map(transactionType -> Arguments.of(blankArg.get()[0], Named.of(transactionType.name(), transactionType)));
        });
    }
}
