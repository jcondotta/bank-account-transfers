package com.jcondotta.bank_account_transfers.interfaces.rest.exception.adapter;

import com.jcondotta.bank_account_transfers.application.ports.output.LocaleResolverPort;
import com.jcondotta.bank_account_transfers.application.ports.output.MessageResolverPort;
import com.jcondotta.bank_account_transfers.application.ports.output.ValidationErrorConverterPort;
import com.jcondotta.bank_account_transfers.interfaces.rest.exception.model.FieldValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ValidationErrorConverterAdapter implements ValidationErrorConverterPort<FieldError, FieldValidationError> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationErrorConverterAdapter.class);

    private final MessageResolverPort messageResolverPort;
    private final LocaleResolverPort localeResolverPort;

    public ValidationErrorConverterAdapter(
        MessageResolverPort messageResolverPort,
        @Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort
    ) {
        this.messageResolverPort = messageResolverPort;
        this.localeResolverPort = localeResolverPort;
    }

    @Override
    public List<FieldValidationError> apply(Collection<FieldError> fieldErrors) {
        if (fieldErrors == null || fieldErrors.isEmpty()) {
            LOGGER.debug("No validation errors to convert.");
            return List.of();
        }

        Locale locale = localeResolverPort.resolveLocale();
        LOGGER.debug("Converting validation messages using locale: {}", locale);

        Map<String, List<String>> groupedErrors = groupAndResolveMessages(fieldErrors, locale);

        return groupedErrors.entrySet().stream()
            .map(entry -> FieldValidationError.of(entry.getKey(), entry.getValue()))
            .toList();
    }

    private Map<String, List<String>> groupAndResolveMessages(Collection<FieldError> fieldErrors, Locale locale) {
        return fieldErrors.stream()
            .collect(Collectors.groupingBy(
                FieldError::getField,
                Collectors.mapping(error -> resolveMessageOrFallback(error, locale), Collectors.toList())
            ));
    }

    private String resolveMessageOrFallback(FieldError error, Locale locale) {
        String messageCode = error.getDefaultMessage();
        Optional<String> resolved = messageResolverPort.resolveMessage(messageCode, locale);

        if (resolved.isEmpty()) {
            LOGGER.warn("Could not resolve message code '{}' for locale '{}'. Using default message.", messageCode, locale);
        }

        return resolved.orElse(messageCode);
    }
}
