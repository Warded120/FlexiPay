package com.ivan.shared.client;

import com.ivan.shared.constant.CurrencyCode;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OpenExchangeClient {

    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    public Double exchange(Double amount, CurrencyCode from, CurrencyCode to) {
        ServiceInstance serviceInstance = discoveryClient.getInstances("open-exchange").get(0);

        String uri = UriComponentsBuilder.fromUri(serviceInstance.getUri())
                .path("/open-exchange/exchange")
                .queryParam("amount", amount)
                .queryParam("from", from)
                .queryParam("to", to)
                .toUriString();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(Double.class);
    }
}