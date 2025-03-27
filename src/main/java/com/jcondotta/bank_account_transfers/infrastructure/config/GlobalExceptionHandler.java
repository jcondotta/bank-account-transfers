package com.jcondotta.bank_account_transfers.infrastructure.config;

import com.jcondotta.bank_account_transfers.infrastructure.adapters.inbound.HttpRequestLocaleResolverAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.Clock;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    @Qualifier("errorMessageSource")
    private MessageSource errorMessageSource;

    private Clock clock;

    @Autowired
    private HttpRequestLocaleResolverAdapter userLocaleResolver;

    public GlobalExceptionHandler(MessageSource errorMessageSource, Clock clock, HttpRequestLocaleResolverAdapter userLocaleResolver) {
        this.errorMessageSource = errorMessageSource;
        this.clock = clock;
        this.userLocaleResolver = userLocaleResolver;
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MissingRequestHeaderException.class)
//    public ResponseEntity<Map<String, Object>> handleMissingRequestHeaderException(HttpServletRequest request, MissingRequestHeaderException ex) {
//        Map<String, Object> errorResponse = Map.of(
//                "timestamp", Instant.now(clock),
//                "status", HttpStatus.BAD_REQUEST.value(),
//                "error", "Bad Request",
//                "message", ex.getMessage(),  // ✅ Include the error message
//                "headerName", ex.getHeaderName(), // ✅ Extra info: Which header is missing?
//                "path", request.getRequestURI()
//        );
//
//        return ResponseEntity.badRequest().body(errorResponse);
//    }
}
