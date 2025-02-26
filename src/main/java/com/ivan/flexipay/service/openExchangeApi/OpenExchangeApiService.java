package com.ivan.flexipay.service.openExchangeApi;

import com.ivan.flexipay.constant.CurrencyCode;

import java.util.Map;

public interface OpenExchangeApiService {
    Map<String, Object> getExchangeRates();
    Double exchange(Double amount, CurrencyCode from, CurrencyCode to);
}
