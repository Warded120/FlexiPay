package com.ivan.flexipay.dto;

import com.ivan.flexipay.constant.CurrencyCode;
import com.ivan.flexipay.validation.ValidCurrencyCode;
import jakarta.validation.constraints.Min;

public record PaymentDto (
   String fromAccount,
   String toAccount,
   @ValidCurrencyCode CurrencyCode fromCurrency,
   @ValidCurrencyCode CurrencyCode toCurrency,
   @Min(value = 0, message = "cannot be less than 0") Double amount
) {}
