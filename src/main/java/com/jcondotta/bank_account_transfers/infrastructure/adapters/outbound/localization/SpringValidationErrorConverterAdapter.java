package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.localization;

import com.jcondotta.bank_account_transfers.application.ports.inbound.LocaleResolverPort;
import com.jcondotta.bank_account_transfers.application.ports.outbound.validation.ValidationErrorConverterPort;
import com.jcondotta.bank_account_transfers.application.ports.outbound.localization.MessageResolverPort;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.FieldValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SpringValidationErrorConverterAdapter implements ValidationErrorConverterPort<FieldError, FieldValidationError> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringValidationErrorConverterAdapter.class);

    private final MessageResolverPort messageResolverPort;
    private final LocaleResolverPort localeResolverPort;

    public SpringValidationErrorConverterAdapter(MessageResolverPort messageResolverPort,
            @Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort) {
        this.messageResolverPort = messageResolverPort;
        this.localeResolverPort = localeResolverPort;
    }

    @Override
    public List<FieldValidationError> apply(Collection<FieldError> fieldErrors) {
        var userLocale = localeResolverPort.resolveLocale();
        LOGGER.debug("Using locale: {}", userLocale);

        Function<FieldError, String> resolveMessage = fieldError -> {
            var messageCode = fieldError.getDefaultMessage();
            var resolvedMessage = messageResolverPort.resolveMessage(messageCode, userLocale);

            if (resolvedMessage.equals(messageCode)) {
                LOGGER.warn("Message code '{}' not found, using default.", messageCode);
            }

            return resolvedMessage;
        };

        return fieldErrors.stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(resolveMessage, Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> new FieldValidationError(entry.getKey(), entry.getValue()))
                .toList();
    }
}
