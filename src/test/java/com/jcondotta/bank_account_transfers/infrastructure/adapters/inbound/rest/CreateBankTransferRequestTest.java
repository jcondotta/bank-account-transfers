package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

class CreateBankTransferRequestTest {

//    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();
//    private static final String RECIPIENT_IBAN_PATRIZIO = TestBankAccount.PATRIZIO.getIban();
//
//    private static final BigDecimal TRANSFER_AMOUNT_TWO_HUNDRED = new BigDecimal("200.00");
//    private static final String CURRENCY_EURO = "EUR";
//    private static final String TRANSFER_REFERENCE = "Invoice 437263";
//
//    private static final Validator VALIDATOR = TestValidatorConfig.getValidator();
//
//    @Test
//    void shouldNotDetectConstraintViolation_whenRequestIsValid() {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertThat(constraintViolations).isEmpty();
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenSenderBankAccountIdIsNull() {
//        var request = new CreateBankTransferRequest(
//                null,
//                RECIPIENT_IBAN_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.senderBankAccountId.notNull", "senderBankAccountId");
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenRecipientIbanIsNull() {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                null,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.recipientIban.notNull", "recipientIban");
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"ES12", "GB1", "DE123"})
//    void shouldDetectConstraintViolation_whenRecipientIbanIsShorterThan15Characters(String tooShortIban) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                tooShortIban,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.recipientIban.size", "recipientIban");
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"ES12345678901234567890123456789012345", "FR763000400003123456789018762345678901"})
//    void shouldDetectConstraintViolation_whenRecipientIbanExceeds34Characters(String tooLongIban) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                tooLongIban,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.recipientIban.size", "recipientIban");
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenAmountIsNull() {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                null,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.amount.notNull", "monetaryAmount");
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"-10.00", "0.00"})
//    void shouldDetectConstraintViolation_whenAmountIsNotPositive(String invalidAmount) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                new BigDecimal(invalidAmount),
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.amount.positive", "monetaryAmount");
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenAmountIsTooLarge() {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                new BigDecimal("1000000000000.00"),
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.amount.invalidPrecision", "monetaryAmount");
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenAmountHasTooManyDecimalPlaces() {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                new BigDecimal("100.123"),
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.amount.invalidPrecision", "monetaryAmount");
//    }
//
//    @Test
//    void shouldDetectMultipleViolations_whenAmountIsNegativeAndExceedsAllowedPrecision() {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                new BigDecimal("-20.300"),
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//
//        assertThat(constraintViolations)
//                .extracting(v -> tuple(v.getMessage(), v.getPropertyPath().toString()))
//                .containsExactlyInAnyOrder(
//                        tuple("transfer.amount.positive", "monetaryAmount"),
//                        tuple("transfer.amount.invalidPrecision", "monetaryAmount")
//                );
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenCurrencyIsNull() {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                null,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.currency.notNull", "currency");
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"usD", "eu1", "1EU", "US9", "123", "USDX", "EURO"})
//    void shouldDetectConstraintViolation_whenCurrencyHasInvalidFormat(String invalidCurrency) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                invalidCurrency,
//                TRANSFER_REFERENCE
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.currency.invalidFormat", "currency");
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(BlankValuesArgumentProvider.class)
//    void shouldNotDetectConstraintViolation_whenReferenceIsBlank(String transferReference) {
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                transferReference
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertThat(constraintViolations).isEmpty();
//    }
//
//    @Test
//    void shouldDetectConstraintViolation_whenTransferReferenceExceeds50Characters() {
//        var veryLongTransferReference = "J".repeat(51);
//
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                veryLongTransferReference
//        );
//
//        var constraintViolations = VALIDATOR.validate(request);
//        assertSingleViolation(constraintViolations, "transfer.reference.tooLong", "reference");
//    }
//
//    private void assertSingleViolation(Set<ConstraintViolation<CreateBankTransferRequest>> constraintViolations, String expectedMessage, String expectedProperty) {
//        assertThat(constraintViolations)
//                .singleElement()
//                .extracting(ConstraintViolation::getMessage, v -> v.getPropertyPath().toString())
//                .containsExactly(expectedMessage, expectedProperty);
//    }
}
