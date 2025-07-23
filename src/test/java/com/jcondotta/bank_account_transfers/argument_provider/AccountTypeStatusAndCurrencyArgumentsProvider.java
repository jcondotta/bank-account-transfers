package com.jcondotta.bank_account_transfers.argument_provider;

import com.jcondotta.bank_account_transfers.domain.bank_account.enums.AccountStatus;
import com.jcondotta.bank_account_transfers.domain.bank_account.enums.AccountType;
import com.jcondotta.bank_account_transfers.domain.shared.valueobjects.Currency;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class AccountTypeStatusAndCurrencyArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return stream(AccountType.values())
            .flatMap(accountType ->
                stream(AccountStatus.values())
                    .flatMap(accountStatus ->
                        stream(Currency.values())
                            .map(currency -> Arguments.of(accountType, accountStatus, currency))
                    )
            );
    }
}
