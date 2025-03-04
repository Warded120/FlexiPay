package com.ivan.flexipay.dto;

import com.ivan.flexipay.constant.CurrencyCode;
import jakarta.validation.constraints.NotNull;

public record CurrencyDto (@NotNull(message = "cannot be null") CurrencyCode currencyCode) {}
