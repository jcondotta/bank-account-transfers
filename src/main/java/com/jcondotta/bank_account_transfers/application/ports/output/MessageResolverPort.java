package com.jcondotta.bank_account_transfers.application.ports.output;

import java.util.Locale;
import java.util.Optional;

@FunctionalInterface
public interface MessageResolverPort {

    Optional<String> resolveMessage(String messageCode, Object[] args, Locale locale);

    default Optional<String> resolveMessage(String messageCode, Locale locale){
        return resolveMessage(messageCode, null, locale);
    }
}