package com.ivan.flexipay.validation;

import com.ivan.flexipay.constant.CurrencyCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, CurrencyCode> {

    @Override
    public boolean isValid(CurrencyCode value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // Or return true if null values should be allowed
        }

        return Arrays.asList(CurrencyCode.values()).contains(value);
    }
}
