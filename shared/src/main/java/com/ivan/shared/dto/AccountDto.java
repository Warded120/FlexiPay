package com.ivan.shared.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public record AccountDto(
        @Size(max = 20, message = "Account number must not exceed 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Account number must contain only letters and digits, no special characters")
        String accountId,

        @Valid
        @UniqueElements(message = "Currencies must be unique")
        @Size(min = 1, message = "Must contain at least one currency")
        List<CurrencyDto> currencies
) {
        public static final String defaultJson = """
            {
              "accountId": "string",
              "currencies": [
                {
                  "currencyCode": "USD"
                }
              ]
            }
            """;
}
