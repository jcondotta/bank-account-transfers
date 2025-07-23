package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
class ErrorMessageResolverAdapterTest {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private static final String DEFAULT_MESSAGE_CODE = "message.code";
    private static final Object[] EMPTY_ARGUMENTS = new Object[]{};

//    @Mock
//    private MessageSource mockMessageSource;
//
//    private MessageResolverPort messageResolver;
//
//    @BeforeEach
//    void beforeEach() {
//        messageResolver = new ErrorMessageResolverAdapter(mockMessageSource);
//    }
//
//    @Test
//    void shouldResolveMessage_whenFindsMessageByMessageCode() {
//        String expectedMessage = "object must not be null.";
//
//        when(mockMessageSource.getMessage(DEFAULT_MESSAGE_CODE, EMPTY_ARGUMENTS, DEFAULT_MESSAGE_CODE, DEFAULT_LOCALE))
//                .thenReturn(expectedMessage);
//
//        String actualMessage = messageResolver.resolveMessage(DEFAULT_MESSAGE_CODE, EMPTY_ARGUMENTS, DEFAULT_LOCALE);
//
//        assertEquals(expectedMessage, actualMessage);
//        verify(mockMessageSource).getMessage(anyString(), any(), anyString(), any());
//    }
//
//    @Test
//    void shouldReturnMessageCode_whenMessageIsNotFound() {
//        String expectedMessage = DEFAULT_MESSAGE_CODE;
//
//        when(mockMessageSource.getMessage(DEFAULT_MESSAGE_CODE, EMPTY_ARGUMENTS, DEFAULT_MESSAGE_CODE, DEFAULT_LOCALE))
//                .thenReturn(expectedMessage);
//
//        String actualMessage = messageResolver.resolveMessage(DEFAULT_MESSAGE_CODE, EMPTY_ARGUMENTS, DEFAULT_LOCALE);
//
//        assertEquals(expectedMessage, actualMessage);
//        verify(mockMessageSource).getMessage(anyString(), any(), anyString(), any());
//    }
}