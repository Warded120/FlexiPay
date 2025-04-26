package com.ivan.openexchange.controller;

import com.ivan.openexchange.service.OpenExchangeApiService;
import com.ivan.shared.constant.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-exchange")
@RequiredArgsConstructor
public class OpenExchangeApiController {
    private final OpenExchangeApiService exchangeService;

    @GetMapping("/exchange")
    public Double exchange(
            @RequestParam Double amount,
            @RequestParam CurrencyCode from,
            @RequestParam CurrencyCode to
    ) {
        return exchangeService.exchange(amount, from, to);
    }
}
