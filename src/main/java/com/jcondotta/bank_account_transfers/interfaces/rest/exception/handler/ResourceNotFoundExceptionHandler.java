package com.jcondotta.bank_account_transfers.interfaces.rest.exception.handler;

import com.jcondotta.bank_account_transfers.application.ports.output.LocaleResolverPort;
import com.jcondotta.bank_account_transfers.application.ports.output.MessageResolverPort;
import com.jcondotta.bank_account_transfers.domain.shared.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.Clock;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ResourceNotFoundExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceNotFoundExceptionHandler.class);

    private final LocaleResolverPort localeResolverPort;
    private final MessageResolverPort messageResolverPort;
    private final Clock clock;

    public ResourceNotFoundExceptionHandler(@Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort,
                                            MessageResolverPort messageResolverPort,
                                            Clock clock) {
        this.localeResolverPort = localeResolverPort;
        this.messageResolverPort = messageResolverPort;
        this.clock = clock;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleBankAccountNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        var locale = localeResolverPort.resolveLocale();
        var message = messageResolverPort.resolveMessage(ex.getMessage(), ex.getIdentifiers(), locale)
            .orElseGet(() -> {
                LOGGER.warn("Message code '{}' not found for locale '{}', using default.", ex.getMessage(), locale);
                return ex.getMessage();
            });

        LOGGER.warn(message);

        var httpStatus404 = HttpStatus.NOT_FOUND;
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus404);
        problemDetail.setTitle(httpStatus404.getReasonPhrase());
        problemDetail.setDetail(message);
        problemDetail.setType(URI.create("/errors/resource-not-found"));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", ZonedDateTime.now(clock));

        return ResponseEntity.status(httpStatus404.value())
            .body(problemDetail);
    }
}
