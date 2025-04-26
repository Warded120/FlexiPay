package com.ivan.shared.dto;

import com.ivan.shared.constant.CurrencyCode;
import com.ivan.shared.validation.ValidCurrencyCode;
import jakarta.validation.constraints.Min;

public record PaymentDto (
   String fromAccount,
   String toAccount,
   @ValidCurrencyCode CurrencyCode fromCurrency,
   @ValidCurrencyCode CurrencyCode toCurrency,
   @Min(value = 0, message = "cannot be less than 0") Double amount
) {}
