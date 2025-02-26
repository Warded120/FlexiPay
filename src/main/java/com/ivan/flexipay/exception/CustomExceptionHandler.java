package com.ivan.flexipay.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {
    //TODO: create exceptions and exception handlers
    /*
    @ExceptionHandler(ExampleException.class)
    public final ResponseEntity<Object> handleFunctionalityNotAvailableException(ExampleException ex,
                                                                                 WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        exceptionResponse.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }
    */
}
