package com.ivan.shared.validation;

import com.ivan.shared.constant.CurrencyCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

/**
 * Validator for {@link CurrencyCode} to ensure it is a valid enum value.
 */
public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, CurrencyCode> {

    @Override
    public boolean isValid(CurrencyCode value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Arrays.asList(CurrencyCode.values()).contains(value);
    }
}

