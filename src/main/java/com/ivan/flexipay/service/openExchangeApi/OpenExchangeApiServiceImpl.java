package com.ivan.flexipay.service.openExchangeApi;

import com.ivan.flexipay.constant.CurrencyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OpenExchangeApiServiceImpl implements OpenExchangeApiService {

    @Value("${currency.api.url}")
    private String apiUrl;

    @Value("${currency.appID}")
    private String appID;

    private final RestTemplate restTemplate;

    @Autowired
    public OpenExchangeApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getExchangeRates() {
        String url = apiUrl + appID;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("rates")) {
            return (Map<String, Object>) response.get("rates");
        }

        return null; //TODO: throw an exception
    }

    //TODO: refactor
    @Override
    public Double exchange(Double amount, CurrencyCode from, CurrencyCode to) {
        Map<String, Object> rates = getExchangeRates();

        if (rates == null || !rates.containsKey(from.toString()) || !rates.containsKey(to.toString())) {
            throw new IllegalArgumentException("Invalid currency codes or exchange rates not available.");
        }

        double fromRate = getSafeDouble(rates.get(from.toString()));
        double toRate = getSafeDouble(rates.get(to.toString()));

        double amountInUsd = amount / fromRate;
        return amountInUsd * toRate;
    }

    private double getSafeDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new IllegalArgumentException("Invalid exchange rate format: " + value);
    }
}