package com.ivan.shared.dto;

import com.ivan.shared.constant.CurrencyCode;
import com.ivan.shared.validation.ValidCurrencyCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DepositDto(
        @ValidCurrencyCode CurrencyCode currency,
        @NotNull(message = "cannot be null") @Min(value = 0, message = "cannot be less than 0") Double amount
) {
}
