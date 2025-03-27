package com.jcondotta.bank_account_transfers.domain.validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public interface Validatable {

    Logger LOGGER = LoggerFactory.getLogger(Validatable.class);

    default void validate(Validator validator) {
        LOGGER.trace("Validating: {} properties", this.getClass().getSimpleName());

        var constraintViolations = validator.validate(this);

        if (!constraintViolations.isEmpty()) {
            String validationMessages = constraintViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            LOGGER.warn("Validation failed for {}: [{}]", this.getClass().getSimpleName(), validationMessages);
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}