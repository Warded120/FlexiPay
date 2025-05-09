package com.ivan.flexipay.exception.handler;

import com.ivan.flexipay.exception.exceptions.AccountAlreadyExistsException;
import com.ivan.flexipay.exception.exceptions.BadRequestException;
import com.ivan.flexipay.exception.exceptions.ExchangeRatesNotAvailableException;
import com.ivan.flexipay.exception.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom exception handler that extends {@link ResponseEntityExceptionHandler}.
 * This class is used to handle exceptions globally and customize error responses.
 */
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private ErrorAttributes errorAttributes;

    /**
     * Retrieves the error attributes for a given web request.
     *
     * @param webRequest The web request that caused the exception.
     * @return A map containing the error attributes, such as the error message.
     * @see ErrorAttributes#getErrorAttributes(WebRequest, ErrorAttributeOptions)
     */
    private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
        return new HashMap<>(errorAttributes.getErrorAttributes(webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)));
    }

    /**
     * Method interceptor for BadRequest-related exceptions such as
     * {@link BadRequestException}
     *
     * @param request Contains details about the occurred exception.
     * @return ResponseEntity which contains the HTTP status and body with the
     *         message of the exception.
     * @author Hrenevych Ivan
     */
    @ExceptionHandler({
            BadRequestException.class,
            AccountAlreadyExistsException.class
    })
    public final ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        log.trace(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    /**
     * Method interceptor for API exceptions such as
     * {@link ExchangeRatesNotAvailableException}
     *
     * @param request Contains details about the occurred exception.
     * @return ResponseEntity which contains the HTTP status and body with the
     *         message of the exception.
     * @author Hrenevych Ivan
     */
    @ExceptionHandler(ExchangeRatesNotAvailableException.class)
    public final ResponseEntity<Object> handleExchangeRatesNotAvailableException(ExchangeRatesNotAvailableException ex,
                                                                                 WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        exceptionResponse.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exceptionResponse);
    }

    /**
     * Method interceptor for API exceptions such as
     * {@link NotFoundException}
     *
     * @param request Contains details about the occurred exception.
     * @return ResponseEntity which contains the HTTP status and body with the
     *         message of the exception.
     * @author Hrenevych Ivan
     */
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex,
                                                                                 WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));
        exceptionResponse.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    /**
     * Method interceptor for jakarta.validation exceptions
     *
     * @param ex      The exception containing validation errors.
     * @param headers HTTP headers for the response.
     * @param status  The HTTP status code associated with the error.
     * @param request The web request that triggered the exception.
     * @return A {@link ResponseEntity} containing an HTTP 400 Bad Request status and
     *         a list of validation error details.
     * @author Hrenevych Ivan
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ValidationExceptionDto> collect =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(ValidationExceptionDto::new)
                        .collect(Collectors.toList());
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(collect);
    }
}
