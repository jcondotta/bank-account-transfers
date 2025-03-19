package com.jcondotta.bank_account_transfers.application.ports.inbound;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.application.ports.outbound.persistence.BankTransferRepositoryPort;
import com.jcondotta.bank_account_transfers.application.services.CreateBankTransferService;
import com.jcondotta.bank_account_transfers.domain.BankTransfer;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestClockConfig;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestValidatorConfig;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CreateBankTransferServiceTest {

    private static final UUID IDEMPOTENCY_KEY = UUID.fromString("8d4c6f5c-3726-4b44-94a7-29a016853fb1");

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();

    private static final String RECIPIENT_NAME_PATRIZIO = TestBankAccount.PATRIZIO.getAccountHolderName();
    private static final String RECIPIENT_IBAN_PATRIZIO = TestBankAccount.PATRIZIO.getIban();

    private static final BigDecimal TRANSFER_AMOUNT_TWO_HUNDRED = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";
    private static final String TRANSFER_REFERENCE = "INVOICE 877635";

    private static final Clock TEST_CLOCK = TestClockConfig.testClockFixedInstant;
    private static final Validator VALIDATOR = TestValidatorConfig.getValidator();

    private CreateBankTransferService createBankTransferService;

    @Mock
    private BankTransferRepositoryPort repository;

    @Mock
    private BankTransfer mockBankTransfer;

    @BeforeEach
    void beforeEach(){
        createBankTransferService = new CreateBankTransferService(repository, VALIDATOR, null);
    }

//    @Test
//    void shouldCreateBankTransfer_whenRequestIsValid(){
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        when(repository.findByIdempotencyKey(any(UUID.class)))
//                .thenReturn(Optional.empty());
//
//        when(repository.save(any(BankTransfer.class)))
//                .thenReturn(mockBankTransfer);
//
//        createBankTransferService.createBankTransfer(IDEMPOTENCY_KEY, request);
//
//        verify(repository).findByIdempotencyKey(IDEMPOTENCY_KEY);
//        verify(repository).save(any(BankTransfer.class));
//    }
//
//    @Test
//    void shouldThrowNullPointerException_whenIdempotencyKeyIsNull(){
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> createBankTransferService.createBankTransfer(null, request))
//                .isInstanceOf(NullPointerException.class)
//                .hasMessage("idempotencyKey.notNull");
//
//        verifyNoInteractions(repository);
//    }
//
//    @Test
//    void shouldThrowConstraintViolationException_whenRequestValidationFails() {
//        var request = new CreateBankTransferRequest(
//                null,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        assertThatThrownBy(() -> createBankTransferService.createBankTransfer(IDEMPOTENCY_KEY, request))
//                .isInstanceOf(ConstraintViolationException.class)
//                .extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
//                .satisfies(violations -> assertThat(violations)
//                        .hasSize(1)
//                        .singleElement()
//                        .satisfies(violation -> Assertions.assertAll(
//                                () -> assertThat(violation.getPropertyPath()).hasToString("senderBankAccountId"),
//                                () -> assertThat(violation.getMessage()).isEqualTo("transfer.senderBankAccountId.notNull")
//                        )));
//
//
//        verifyNoInteractions(repository);
//    }
//
//    @Test
//    void shouldReturnBankTransferDTO_whenRequestIsDuplicated(){
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        when(repository.findByIdempotencyKey(any(UUID.class))).thenReturn(Optional.of(mockBankTransfer));
//
//        createBankTransferService.createBankTransfer(IDEMPOTENCY_KEY, request);
//
//        verify(repository).findByIdempotencyKey(any(UUID.class));
//        verify(repository, never()).save(any(BankTransfer.class));
//    }
}