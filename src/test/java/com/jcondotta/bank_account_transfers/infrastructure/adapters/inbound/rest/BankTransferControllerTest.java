package com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.rest;

import com.jcondotta.bank_account_transfers.TestBankAccount;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CreateBankTransferController.class)
@ExtendWith(MockitoExtension.class)
class BankTransferControllerTest {

    private static final UUID BANK_ACCOUNT_ID_JEFFERSON = TestBankAccount.JEFFERSON.getBankAccountId();
    private static final String RECIPIENT_NAME_PATRIZIO = TestBankAccount.PATRIZIO.getAccountHolderName();
    private static final String RECIPIENT_IBAN_PATRIZIO = TestBankAccount.PATRIZIO.getIban();

    private static final BigDecimal TRANSFER_AMOUNT_TWO_HUNDRED = new BigDecimal("200.00");
    private static final String CURRENCY_EURO = "EUR";
    private static final String TRANSFER_REFERENCE = "Playstation 4";

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private CreateBankTransferService createBankTransferService;
//
//    @MockitoBean
//    private ValidationErrorMapper<FieldError, FieldErrorMessage> validationErrorMapper;
//
//    @MockitoBean
//    private Clock clock;
//
//    @MockitoBean
//    private SpringLocaleResolverAdapter localeResolverPort;
//
//    @MockitoBean
//    private SpringErrorMessageResolver messageResolver;
//
//    @MockitoBean("errorMessageSource")
//    private MessageSource messageSource;
//
//    @Mock
//    BankTransferDTO bankTransferDTO;
//
//    @Test
//    void shouldCreateBankTransferSuccessfully() throws Exception {
//        UUID idempotencyKey = UUID.randomUUID();
//        var request = new CreateBankTransferRequest(
//                BANK_ACCOUNT_ID_JEFFERSON,
//                RECIPIENT_IBAN_PATRIZIO,
//                RECIPIENT_NAME_PATRIZIO,
//                TRANSFER_AMOUNT_TWO_HUNDRED,
//                CURRENCY_EURO,
//                TRANSFER_REFERENCE
//        );
//
//        when(bankTransferDTO.financialTransactionId()).thenReturn(UUID.randomUUID());
//        when(createBankTransferService.createBankTransfer(any(UUID.class), any(CreateBankTransferRequest.class)))
//                .thenReturn(bankTransferDTO);
//
//        mockMvc.perform(post("/" + BankTransferAPIPaths.BASE_V1_PATH)
//                        .header("Idempotency-Key", idempotencyKey.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(header().exists(HttpHeaders.LOCATION));
//    }
}
