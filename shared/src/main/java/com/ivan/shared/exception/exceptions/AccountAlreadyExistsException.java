package com.ivan.shared.exception.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception that we get when user is trying to pass already existing account id.
 */
@StandardException
public class AccountAlreadyExistsException extends BadRequestException {
}
