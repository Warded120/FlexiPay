package com.ivan.flexipay.dto.account;

import com.ivan.flexipay.dto.CurrencyDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record AccountDto(
        @Size(max = 20, message = "Account number must not exceed 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Account number must contain only letters and digits, no special characters")
        String accountId,

        @Valid
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
