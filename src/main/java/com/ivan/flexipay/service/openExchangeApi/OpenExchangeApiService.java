package com.ivan.flexipay.service.openExchangeApiService;

import com.ivan.flexipay.constant.CurrencyCode;

public interface OpenExchangeApiService {
    Double exchange(Double amount, CurrencyCode from, CurrencyCode to);
}
