package com.ivan.flexipay.dto;

import com.ivan.flexipay.constant.CurrencyCode;
import com.ivan.flexipay.validation.ValidCurrencyCode;

public record CurrencyDto (
        @ValidCurrencyCode CurrencyCode currencyCode
) {}
