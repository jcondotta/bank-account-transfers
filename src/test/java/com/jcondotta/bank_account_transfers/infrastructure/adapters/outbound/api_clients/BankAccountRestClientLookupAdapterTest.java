package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import com.jcondotta.bank_account_transfers.TestBankAccountDTO;
import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts.BankAccountDTO;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestRestClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class BankAccountRestClientLookupAdapterTest {

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();
    private static final String BANK_ACCOUNT_IBAN_JEFFERSON = TestBankAccount.JEFFERSON.getIban();

    private static final String BANK_ACCOUNT_BY_IBAN_PATH = "/api/v1/bank-accounts/iban/{iban}";
    private static final int SERVER_TIMEOUT_IN_MILLISECONDS = 100;

    private final MockWebServer mockWebServer = new MockWebServer();

    private final RestClient restClient = TestRestClient.builder()
            .readTimeout(SERVER_TIMEOUT_IN_MILLISECONDS)
            .build();

    private BankAccountLookupPort<String, BankAccountDTO> bankAccountLookupPort;

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer.start();

        var serverURL = mockWebServer.url("").toString();
        var bankAccountServiceConfig = new BankAccountServiceV1Config(serverURL, BANK_ACCOUNT_BY_IBAN_PATH);
        bankAccountLookupPort = new BankAccountRestClientLookupAdapter(restClient, bankAccountServiceConfig);
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldReturnBankAccountDTO_whenBankAccountIbanExists() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(TestBankAccountDTO.JEFFERSON.getJson())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        var response = bankAccountLookupPort.findBankAccount(BANK_ACCOUNT_IBAN_JEFFERSON);

        assertThat(response)
                .hasValueSatisfying(bankAccountDTO -> assertAll(
                        () -> assertThat(bankAccountDTO.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID_JEFFERSON),
                        () -> assertThat(bankAccountDTO.iban()).isEqualTo(BANK_ACCOUNT_IBAN_JEFFERSON))
                );

        var expectedPathURI = UriComponentsBuilder.fromPath(BANK_ACCOUNT_BY_IBAN_PATH).buildAndExpand(BANK_ACCOUNT_IBAN_JEFFERSON).toUri();

        var recordedRequest = mockWebServer.takeRequest();
        assertAll(
                () -> assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name()),
                () -> assertThat(recordedRequest.getPath()).isEqualTo(expectedPathURI.getPath()),
                () -> assertThat(recordedRequest.getHeader("Accept")).isEqualTo(MediaType.APPLICATION_JSON_VALUE)
        );
    }

    @Test
    void shouldReturnEmptyOptional_whenServerReturns404NotFound() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
        );

        var nonExistentBankAccountIban = "non-existent-bank-account-iban";
        var response = bankAccountLookupPort.findBankAccount(nonExistentBankAccountIban);
        assertThat(response).isEmpty();

        var expectedPathURI = UriComponentsBuilder.fromPath(BANK_ACCOUNT_BY_IBAN_PATH)
                .buildAndExpand(nonExistentBankAccountIban).toUri();

        var recordedRequest = mockWebServer.takeRequest();
        assertAll(
                () -> assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name()),
                () -> assertThat(recordedRequest.getPath()).isEqualTo(expectedPathURI.getPath()),
                () -> assertThat(recordedRequest.getHeader("Accept")).isEqualTo(MediaType.APPLICATION_JSON_VALUE)
        );
    }

    @Test
    void shouldReturnEmptyOptional_whenServerReturns500InternalServerError() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );

        var response = bankAccountLookupPort.findBankAccount(BANK_ACCOUNT_IBAN_JEFFERSON);
        assertThat(response).isEmpty();

        var expectedPathURI = UriComponentsBuilder.fromPath(BANK_ACCOUNT_BY_IBAN_PATH)
                .buildAndExpand(BANK_ACCOUNT_IBAN_JEFFERSON).toUri();

        var recordedRequest = mockWebServer.takeRequest();
        assertAll(
                () -> assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name()),
                () -> assertThat(recordedRequest.getPath()).isEqualTo(expectedPathURI.getPath()),
                () -> assertThat(recordedRequest.getHeader("Accept")).isEqualTo(MediaType.APPLICATION_JSON_VALUE)
        );
    }

    @Test
    void shouldTimeout_whenServerTakesTooLong() {
        var bodyDelay = SERVER_TIMEOUT_IN_MILLISECONDS + 100;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(TestBankAccountDTO.JEFFERSON.getJson())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBodyDelay(bodyDelay, TimeUnit.MILLISECONDS)
        );

        var response = bankAccountLookupPort.findBankAccount(BANK_ACCOUNT_IBAN_JEFFERSON);
        assertThat(response).isEmpty();
    }
}