package com.jcondotta.bank_account_transfers.application.ports.output;

import java.util.Locale;

@FunctionalInterface
public interface LocaleResolverPort {
    Locale resolveLocale();
}
