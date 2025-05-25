package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.TestBankAccountDTO;
import com.jcondotta.bank_account_transfers.infrastructure.PostgresTestContainer;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.BankTransferAPIConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.util.Locale;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("test")
@ContextConfiguration(initializers = PostgresTestContainer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateBankTransferControllerIT {

    private static final MockWebServer MOCK_WEB_SERVER = new MockWebServer();

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();

    private static final String RECIPIENT_IBAN_PATRIZIO = TestBankAccount.PATRIZIO.getIban();

    private static final BigDecimal TRANSFER_AMOUNT_TWO_HUNDRED = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";
    private static final String TRANSFER_REFERENCE = "Rent 04/2025";

    private RequestSpecification requestSpecification;

    @Autowired
    @Qualifier("errorMessageSource")
    private MessageSource errorMessageSource;

    @Inject
    private BankTransferAPIConfig bankTransferAPIConfig;

    @Inject
    Clock testClockUTC;

    @BeforeAll
    static void beforeAll() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        MOCK_WEB_SERVER.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        MOCK_WEB_SERVER.shutdown();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("bank-account-service.api.v1.base-url",
                () -> MOCK_WEB_SERVER.url("").toString()
        );
    }

    @BeforeEach
    void beforeEach(@LocalServerPort int port) {
        requestSpecification = given()
                .baseUri("http://localhost:" + port)
                .basePath(bankTransferAPIConfig.getRootPath())
                .contentType(ContentType.JSON);
    }

    @Test
    void shouldCreateBankTransfer_whenRequestIsValid() {
        MOCK_WEB_SERVER.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(TestBankAccountDTO.PATRIZIO.getJson())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        );

        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var validatableResponse = given()
                .spec(requestSpecification)
                .body(request)
            .when()
                .post()
            .then()
                .statusCode(HttpStatus.CREATED.value());

        System.out.println(validatableResponse);

//        var createdBankTransferDTO = validatableResponse.extract()
//                .as(BankTransferDTO.class);
//
//        assertAll(
//                () -> assertThat(createdBankTransferDTO.bankTransferId()).isNotNull(),
//                () -> assertThat(createdBankTransferDTO.reference()).isEqualTo(request.reference()),
//                () -> assertThat(createdBankTransferDTO.createdAt()).isEqualTo(Instant.now(testClockUTC))
//        );
//
//        String locationHeader = validatableResponse.extract().header("Location");
//        assertThat(locationHeader).isNotBlank();

//        var fetchedBankTransferDTO = given()
//                .spec(requestSpecification
//                        .basePath(locationHeader))
//            .when()
//                .get()
//            .then()
//                .statusCode(HttpStatus.OK.value())
//                    .extract()
//                        .as(BankTransferDTO.class);
//
//        assertThat(createdBankTransferDTO)
//                .usingRecursiveComparison()
//                .isEqualTo(fetchedBankTransferDTO);
    }

    @Test
    void shouldReturn400BadRequest_whenSenderBankAccountIdIsNull() {
        var request = new CreateBankTransferRequest(
                null,
                RECIPIENT_IBAN_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        var response = given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("senderBankAccountId"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.senderBankAccountId.notNull", null, Locale.ENGLISH)))
                .extract().asString();

        System.out.println(response);

    }

    @Test
    void shouldReturn400BadRequest_whenRecipientIbanIsNull() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                null,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("recipientIban"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.recipientIban.notNull", null, Locale.ENGLISH)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ES12", "GB1", "DE123"})
    void shouldReturn400BadRequest_whenRecipientIbanIsShorterThan15Characters(String tooShortIban) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                tooShortIban,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("recipientIban"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.recipientIban.size", null, Locale.ENGLISH)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ES12345678901234567890123456789012345", "FR763000400003123456789018762345678901"})
    void shouldReturn400BadRequest_whenRecipientIbanExceeds34Characters(String tooLongIban) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                tooLongIban,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("recipientIban"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.recipientIban.size", null, Locale.ENGLISH)));
    }


    @Test
    void shouldReturn400BadRequest_whenAmountIsNull() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                null,
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("amount"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.amount.notNull", null, Locale.ENGLISH)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-10.00", "0.00"})
    void shouldReturn400BadRequest_whenAmountIsNotPositive(String invalidAmount) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                new BigDecimal(invalidAmount),
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("amount"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.amount.positive", null, Locale.ENGLISH)));
    }

    @Test
    void shouldReturn400BadRequest_whenAmountIsTooLarge() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                new BigDecimal("1000000000000.00"),
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("amount"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.amount.invalidPrecision", null, Locale.ENGLISH)));
    }

    @Test
    void shouldReturn400BadRequest_whenAmountHasTooManyDecimalPlaces() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                new BigDecimal("100.123"),
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("amount"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.amount.invalidPrecision", null, Locale.ENGLISH)));
    }

    @Test
    void shouldReturn400BadRequest_whenAmountIsNegativeAndExceedsAllowedPrecision() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                new BigDecimal("-20.300"),
                CURRENCY_EURO,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("amount"))
                .body("errors[0].messages", hasSize(2))
                .body("errors[0].messages", containsInAnyOrder(
                        errorMessageSource.getMessage("transfer.amount.positive", null, Locale.ENGLISH),
                        errorMessageSource.getMessage("transfer.amount.invalidPrecision", null, Locale.ENGLISH)
                ));
    }

    @Test
    void shouldReturn400BadRequest_whenCurrencyIsNull() {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                null,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("currency"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.currency.notNull", null, Locale.ENGLISH)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"usD", "eu1", "1EU", "US9", "123", "USDX", "EURO"})
    void shouldReturn400BadRequest_whenCurrencyHasInvalidFormat(String invalidCurrency) {
        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                invalidCurrency,
                TRANSFER_REFERENCE
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("currency"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.currency.invalidFormat", null, Locale.ENGLISH)));
    }

    @Test
    void shouldReturn400BadRequest_whenTransferReferenceExceeds50Characters() {
        var veryLongTransferReference = "J".repeat(51);

        var request = new CreateBankTransferRequest(
                BANK_ACCOUNT_ID_JEFFERSON,
                RECIPIENT_IBAN_PATRIZIO,
                TRANSFER_AMOUNT_TWO_HUNDRED,
                CURRENCY_EURO,
                veryLongTransferReference
        );

        given()
            .spec(requestSpecification)
            .body(request)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].field", equalTo("reference"))
                .body("errors[0].messages", hasSize(1))
                .body("errors[0].messages[0]", equalTo(
                        errorMessageSource.getMessage("transfer.reference.tooLong", null, Locale.ENGLISH)));
    }
}
