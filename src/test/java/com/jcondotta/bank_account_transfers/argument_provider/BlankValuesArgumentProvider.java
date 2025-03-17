package com.jcondotta.bank_account_transfers.argument_provider;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

public class BlankValuesArgumentProvider implements ArgumentsProvider {

    private static final String DISPLAY_EMPTY_STRING = "\"\"";
    private static final String DISPLAY_BLANK_STRING = "\"   \"";
    private static final String DISPLAY_NULL = "null";

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(Named.of(DISPLAY_EMPTY_STRING, StringUtils.EMPTY)),
                Arguments.of(Named.of(DISPLAY_BLANK_STRING, "   ")),
                Arguments.of(Named.of(DISPLAY_NULL, null))
        );
    }
}