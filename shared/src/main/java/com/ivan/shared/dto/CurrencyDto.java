package com.ivan.shared.dto;

import com.ivan.shared.constant.CurrencyCode;
import com.ivan.shared.validation.ValidCurrencyCode;

public record CurrencyDto (
        @ValidCurrencyCode CurrencyCode currencyCode
) {}
