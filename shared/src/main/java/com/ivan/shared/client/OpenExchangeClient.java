package com.ivan.shared.client;

import com.ivan.shared.constant.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OpenExchangeClient {

    private final RestTemplate restTemplate;
    private static final String EXCHANGE_URL = "http://localhost:8083/open-exchange/exchange";

    public Double exchange(Double amount, CurrencyCode from, CurrencyCode to) {
        String uri = UriComponentsBuilder.fromUriString(EXCHANGE_URL)
                .queryParam("amount", amount)
                .queryParam("from", from)
                .queryParam("to", to)
                .toUriString();

        return restTemplate.getForObject(uri, Double.class);
    }

}