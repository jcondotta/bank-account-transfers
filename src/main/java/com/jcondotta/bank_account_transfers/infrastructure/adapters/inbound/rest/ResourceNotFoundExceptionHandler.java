package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.application.ports.inbound.LocaleResolverPort;
import com.jcondotta.bank_account_transfers.application.ports.outbound.localization.MessageResolverPort;
import com.jcondotta.bank_account_transfers.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.Clock;
import java.time.Instant;

@ControllerAdvice
public class ResourceNotFoundExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceNotFoundExceptionHandler.class);

    private final MessageResolverPort messageResolverPort;
    private final LocaleResolverPort localeResolverPort;
    private final Clock clock;

    public ResourceNotFoundExceptionHandler(@Qualifier("httpRequestLocaleResolver") LocaleResolverPort localeResolverPort,
                                            MessageResolverPort messageResolverPort, Clock clock) {
        this.messageResolverPort = messageResolverPort;
        this.clock = clock;
        this.localeResolverPort = localeResolverPort;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Problem> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        LOGGER.info("Resource not found: {} {}={}", ex.getMessage(), ex.getField(), ex.getValue());

        var userLocale = localeResolverPort.resolveLocale();
        var messageArguments = new Object[]{ex.getField(), ex.getValue()};
        var errorMessage = messageResolverPort.resolveMessage(ex.getMessage(), messageArguments, userLocale);

        Problem problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .withTitle("Resource Not Found")
                .withDetail(errorMessage)
                .with("timestamp", Instant.now(clock))
                .build();

        return ResponseEntity
                .status(Status.NOT_FOUND.getStatusCode())
                .body(problem);
    }
}
