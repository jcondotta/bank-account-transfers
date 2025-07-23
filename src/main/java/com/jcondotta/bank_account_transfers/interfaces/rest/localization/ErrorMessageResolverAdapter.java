package com.jcondotta.bank_account_transfers.interfaces.rest.localization;

import com.jcondotta.bank_account_transfers.application.ports.output.MessageResolverPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
public class ErrorMessageResolverAdapter implements MessageResolverPort {

    private final MessageSource errorMessageSource;

    public ErrorMessageResolverAdapter(@Qualifier("errorMessageSource") MessageSource errorMessageSource) {
        this.errorMessageSource = errorMessageSource;
    }

    @Override
    public Optional<String> resolveMessage(String messageCode, Object[] args, Locale locale) {
        return Optional.ofNullable(
            errorMessageSource.getMessage(messageCode, args, messageCode, locale)
        );
    }
}
