package com.jcondotta.bank_account_transfers.application.ports.outbound.localization;

import java.util.Locale;

@FunctionalInterface
public interface MessageResolverPort {

    String resolveMessage(String messageCode, Object[] args, Locale locale);

    default String resolveMessage(String messageCode, Locale locale){
        return resolveMessage(messageCode, null, locale);
    }
}