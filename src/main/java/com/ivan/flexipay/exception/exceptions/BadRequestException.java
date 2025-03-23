package com.ivan.flexipay.exception.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception that we get when user is trying to pass bad request.
 */
@StandardException
public class BadRequestException extends RuntimeException {
}
