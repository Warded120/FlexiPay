package com.ivan.flexipay.service.openExchangeApiService;

import com.ivan.flexipay.constant.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenExchangeApiServiceImpl implements OpenExchangeApiService {

    @Value("${currency.api.url}")
    private String apiUrl;

    @Value("${currency.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public OpenExchangeApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Double exchange(Double amount, CurrencyCode from, CurrencyCode to) {
        String url = apiUrl + "/convert" + "/" + amount + "/" + from.name() + "/" + to.name() + "?app_id=" + apiKey;

        try {
            Double response = restTemplate.getForObject(url, Double.class);
            return response != null ? response : 0.0;
        } catch (Exception e) {
            return null;  //TODO: throw a custom exception and handle it
        }
    }
}