package com.jcondotta.bank_account_transfers.application.services;

import com.jcondotta.bank_account_transfers.application.ports.outbound.persistence.BankTransferRepositoryPort;
import com.jcondotta.bank_account_transfers.application.usecases.CreateBankTransferUseCase;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest.CreateBankTransferRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CreateBankTransferService implements CreateBankTransferUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBankTransferService.class);

    private final BankTransferRepositoryPort bankTransferRepositoryPort;
    private final Validator validator;
    private final CreateInternalBankTransferService createInternalBankTransferService;

    public CreateBankTransferService(BankTransferRepositoryPort bankTransferRepositoryPort, Validator validator, CreateInternalBankTransferService createInternalBankTransferService) {
        this.bankTransferRepositoryPort = bankTransferRepositoryPort;
        this.validator = validator;
        this.createInternalBankTransferService = createInternalBankTransferService;
    }

    @Override
    public BankTransferDTO createBankTransfer(UUID idempotencyKey, CreateBankTransferRequest request) {
        Objects.requireNonNull(idempotencyKey, "idempotencyKey.notNull");

        LOGGER.info("Processing bank transfer with Idempotency-Key: {}", idempotencyKey, StructuredArguments.f(request));
        var constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            var validationMessages = constraintViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            LOGGER.warn("Validation failed with Idempotency-Key: {}. Violations: [{}]", idempotencyKey,
                    validationMessages, StructuredArguments.f(request));

            throw new ConstraintViolationException(constraintViolations);
        }

        return bankTransferRepositoryPort.findByIdempotencyKey(idempotencyKey)
                .map(BankTransferDTO::new)
                .orElseGet(() -> {
                    var bankTransfer = createInternalBankTransferService.processTransaction(idempotencyKey, request);
                    bankTransferRepositoryPort.save(bankTransfer);

                    return new BankTransferDTO(bankTransfer);
                });
    }
}
