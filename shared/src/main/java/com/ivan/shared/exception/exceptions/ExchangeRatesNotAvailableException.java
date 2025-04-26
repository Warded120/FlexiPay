package com.ivan.shared.exception.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception that we get when currency exchange API is not available
 */
@StandardException
public class ExchangeRatesNotAvailableException extends RuntimeException {
}
