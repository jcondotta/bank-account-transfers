package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankAccountRestClientLookupAdapterTest {

//    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();
//    private static final String BANK_ACCOUNT_IBAN_JEFFERSON = TestBankAccount.JEFFERSON.getIban();
//
//    private static final String BANK_ACCOUNT_BY_IBAN_PATH = "/api/v1/bank-accounts/iban/{iban}";
//    private static final int SERVER_TIMEOUT_IN_MILLISECONDS = 100;
//
//    private final MockWebServer mockWebServer = new MockWebServer();
//
//    private final RestClient restClient = TestRestClient.builder()
//            .readTimeout(SERVER_TIMEOUT_IN_MILLISECONDS)
//            .build();
//
//    private BankAccountLookupPort<String, BankAccountDTO> bankAccountLookupPort;
//
//    @BeforeEach
//    void beforeEach() throws IOException {
//        mockWebServer.start();
//
//        var serverURL = mockWebServer.url("").toString();
//        var bankAccountServiceConfig = new BankAccountServiceV1Config(serverURL, BANK_ACCOUNT_BY_IBAN_PATH);
//        bankAccountLookupPort = new BankAccountRestClientLookupAdapter(restClient, bankAccountServiceConfig);
//    }
//
//    @AfterEach
//    void afterEach() throws IOException {
//        mockWebServer.shutdown();
//    }
//
//    @Test
//    void shouldReturnBankAccountDTO_whenBankAccountIbanExists() throws InterruptedException {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.OK.value())
//                .setBody(TestBankAccountDTO.JEFFERSON.getJson())
//                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//        );
//
//        var response = bankAccountLookupPort.findBankAccount(BANK_ACCOUNT_IBAN_JEFFERSON);
//
//        assertThat(response)
//                .hasValueSatisfying(bankAccountDTO -> assertAll(
//                        () -> assertThat(bankAccountDTO.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID_JEFFERSON),
//                        () -> assertThat(bankAccountDTO.iban()).isEqualTo(BANK_ACCOUNT_IBAN_JEFFERSON))
//                );
//
//        var expectedPathURI = UriComponentsBuilder.fromPath(BANK_ACCOUNT_BY_IBAN_PATH).buildAndExpand(BANK_ACCOUNT_IBAN_JEFFERSON).toUri();
//
//        var recordedRequest = mockWebServer.takeRequest();
//        assertAll(
//                () -> assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name()),
//                () -> assertThat(recordedRequest.getPath()).isEqualTo(expectedPathURI.getPath()),
//                () -> assertThat(recordedRequest.getHeader("Accept")).isEqualTo(MediaType.APPLICATION_JSON_VALUE)
//        );
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerReturns404NotFound() throws InterruptedException {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.NOT_FOUND.value())
//        );
//
//        var nonExistentBankAccountIban = "non-existent-bank-account-iban";
//        var response = bankAccountLookupPort.findBankAccount(nonExistentBankAccountIban);
//        assertThat(response).isEmpty();
//
//        var expectedPathURI = UriComponentsBuilder.fromPath(BANK_ACCOUNT_BY_IBAN_PATH)
//                .buildAndExpand(nonExistentBankAccountIban).toUri();
//
//        var recordedRequest = mockWebServer.takeRequest();
//        assertAll(
//                () -> assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name()),
//                () -> assertThat(recordedRequest.getPath()).isEqualTo(expectedPathURI.getPath()),
//                () -> assertThat(recordedRequest.getHeader("Accept")).isEqualTo(MediaType.APPLICATION_JSON_VALUE)
//        );
//    }
//
//    @Test
//    void shouldReturnEmptyOptional_whenServerReturns500InternalServerError() throws InterruptedException {
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//        );
//
//        var response = bankAccountLookupPort.findBankAccount(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//
//        var expectedPathURI = UriComponentsBuilder.fromPath(BANK_ACCOUNT_BY_IBAN_PATH)
//                .buildAndExpand(BANK_ACCOUNT_IBAN_JEFFERSON).toUri();
//
//        var recordedRequest = mockWebServer.takeRequest();
//        assertAll(
//                () -> assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name()),
//                () -> assertThat(recordedRequest.getPath()).isEqualTo(expectedPathURI.getPath()),
//                () -> assertThat(recordedRequest.getHeader("Accept")).isEqualTo(MediaType.APPLICATION_JSON_VALUE)
//        );
//    }
//
//    @Test
//    void shouldTimeout_whenServerTakesTooLong() {
//        var bodyDelay = SERVER_TIMEOUT_IN_MILLISECONDS + 100;
//
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.OK.value())
//                .setBody(TestBankAccountDTO.JEFFERSON.getJson())
//                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .setBodyDelay(bodyDelay, TimeUnit.MILLISECONDS)
//        );
//
//        var response = bankAccountLookupPort.findBankAccount(BANK_ACCOUNT_IBAN_JEFFERSON);
//        assertThat(response).isEmpty();
//    }
}