package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound;

import com.jcondotta.bank_account_transfers.application.ports.outbound.localization.MessageResolverPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorMessageResolverAdapterTest {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private static final String DEFAULT_MESSAGE_CODE = "message.code";
    private static final Object[] EMPTY_ARGUMENTS = new Object[]{};

    @Mock
    private MessageSource mockMessageSource;

    private MessageResolverPort messageResolver;

    @BeforeEach
    void beforeEach() {
        messageResolver = new ErrorMessageResolverAdapter(mockMessageSource);
    }

    @Test
    void shouldResolveMessage_whenFindsMessageByMessageCode() {
        String expectedMessage = "object must not be null.";

        when(mockMessageSource.getMessage(DEFAULT_MESSAGE_CODE, EMPTY_ARGUMENTS, DEFAULT_MESSAGE_CODE, DEFAULT_LOCALE))
                .thenReturn(expectedMessage);

        String actualMessage = messageResolver.resolveMessage(DEFAULT_MESSAGE_CODE, EMPTY_ARGUMENTS, DEFAULT_LOCALE);

        assertEquals(expectedMessage, actualMessage);
        verify(mockMessageSource).getMessage(anyString(), any(), anyString(), any());
    }

    @Test
    void shouldReturnMessageCode_whenMessageIsNotFound() {
        String expectedMessage = DEFAULT_MESSAGE_CODE;

        when(mockMessageSource.getMessage(DEFAULT_MESSAGE_CODE, EMPTY_ARGUMENTS, DEFAULT_MESSAGE_CODE, DEFAULT_LOCALE))
                .thenReturn(expectedMessage);

        String actualMessage = messageResolver.resolveMessage(DEFAULT_MESSAGE_CODE, EMPTY_ARGUMENTS, DEFAULT_LOCALE);

        assertEquals(expectedMessage, actualMessage);
        verify(mockMessageSource).getMessage(anyString(), any(), anyString(), any());
    }
}