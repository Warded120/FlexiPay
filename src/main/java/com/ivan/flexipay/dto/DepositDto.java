package com.ivan.flexipay.dto;

import com.ivan.flexipay.constant.CurrencyCode;
import com.ivan.flexipay.validation.ValidCurrencyCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DepositDto(
        @ValidCurrencyCode CurrencyCode currency,
        @NotNull(message = "cannot be null") @Min(value = 0, message = "cannot be less than 0") Double amount
) {
}
