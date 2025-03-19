package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestRestClient;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RestClientBankAccountLookupAdapterTest {

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();
    private static final String BANK_ACCOUNT_IBAN_JEFFERSON = TestBankAccount.JEFFERSON.getIban();

    private static final String FIND_BANK_ACCOUNT_BY_IBAN_PATH = "/api/v1/bank-accounts/iban/{iban}";

    private final MockWebServer mockWebServer = new MockWebServer();
    private final RestClient restClient = TestRestClient.builder()
            .readTimeout(100)
            .build();

    private BankAccountLookupPort bankAccountLookupPort;

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer.start();

        var findBankAccountURL = mockWebServer.url(FIND_BANK_ACCOUNT_BY_IBAN_PATH).toString();
        var decodedFindBankAccountURL = URLDecoder.decode(findBankAccountURL, StandardCharsets.UTF_8);
//        bankAccountLookupPort = new RestClientBankAccountLookupAdapter(restClient, new BankAccountServiceV1Config("", decodedFindBankAccountURL));
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

//    @Test
//    void shouldTimeout_whenServerTakesTooLong() {
//        assertThrows(Exception.class, () -> {
//            restClient.get()
//                    .uri("https://httpstat.us/200?sleep=5000") // 🔥 This URL simulates a 5s delay
//                    .retrieve()
//                    .toBodilessEntity();
//        });
//    }
//
//    @Test
//    void shouldReturnBankAccount_whenBankAccountIbanExists() throws InterruptedException {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.OK.value())
//                .setBody(TestBankAccountDTO.JEFFERSON.getJsonBankAccountDTO())
//                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);
//
//        assertThat(response)
//                .hasValueSatisfying(bankAccountDTO -> assertAll(
//                        () -> assertThat(bankAccountDTO.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID_JEFFERSON),
//                        () -> assertThat(bankAccountDTO.iban()).isEqualTo(BANK_ACCOUNT_IBAN_JEFFERSON))
//                );
//
//        var expectedPath = FIND_BANK_ACCOUNT_BY_IBAN_PATH.replace("{iban}", BANK_ACCOUNT_IBAN_JEFFERSON);
//
//        var recordedRequest = mockWebServer.takeRequest();
//        assertAll(
//                () -> assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name()),
//                () -> assertThat(recordedRequest.getPath()).isEqualTo(expectedPath),
//                () -> assertThat(recordedRequest.getHeader("Accept")).isEqualTo(APPLICATION_JSON_VALUE)
//        );
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenClientCannotConnect() {
//        mockWebServer.enqueue(new MockResponse()
//                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerClosesConnection() {
//        mockWebServer.enqueue(new MockResponse()
//                .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenClientCannotReachServer() throws IOException {
//        mockWebServer.shutdown();
//
//        var response = bankAccountLookupPort.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerReturns400() {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.BAD_REQUEST.value())
//                .setBody("{\"error\":\"Invalid IBAN format\"}")
//                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban("invalid-iban");
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerReturns401() {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.UNAUTHORIZED.value())
//                .setBody("{\"error\":\"Unauthorized\"}")
//                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerReturns404() {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.NOT_FOUND.value())
//                .setBody("{\"error\":\"Bank account not found\"}")
//                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban("non-existent-iban");
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerReturns429() {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.TOO_MANY_REQUESTS.value())
//                .setBody("{\"error\":\"Rate limit exceeded\"}")
//                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerReturns500() {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .setBody("{\"error\":\"Unexpected server error\"}")
//                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerReturns503() {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value())
//                .setBody("{\"error\":\"Service is currently unavailable\"}")
//                .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerTimeoutOccurs() {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.GATEWAY_TIMEOUT.value())
//                .setBody("Gateway Timeout")
//                .addHeader(CONTENT_TYPE, TEXT_PLAIN_VALUE)
//                .setBodyDelay(150, TimeUnit.MILLISECONDS)
//        );
//
//        var response = bankAccountLookupPort.findBankAccountByIban(TestBankAccount.JEFFERSON.getIban());
//        assertThat(response).isEmpty();
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenBankAccountAPIReturnsUnexpectedContentType() {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.OK.value())
//                .setBody("<xml><bankAccountId>12345</bankAccountId></xml>") // 🔴 Unexpected XML
//                .addHeader(CONTENT_TYPE, "application/xml"));
//
//        var response = bankAccountLookupPort.findBankAccountByIban(TestBankAccount.JEFFERSON.getIban());
//
//        assertThat(response).isEmpty();
//    }

//    @Test
//    void shouldRetryBeforeReturningEmpty_whenBankAccountAPIIsUnavailable() {
//        mockWebServer.enqueue(new MockResponse().setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value())); // First attempt fails
//        mockWebServer.enqueue(new MockResponse().setResponseCode(HttpStatus.OK.value()).setBody(TestBankAccountDTO.JEFFERSON.getJsonBankAccountDTO()).addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)); // Second attempt succeeds
//
//        var response = bankAccountLookupPort.findBankAccountByIban(TestBankAccount.JEFFERSON.getIban());
//
//        assertThat(response).isPresent();
//    }

}