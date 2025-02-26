package com.ivan.flexipay.controller;

import com.ivan.flexipay.constant.CurrencyCode;
import com.ivan.flexipay.service.openExchangeApi.OpenExchangeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    OpenExchangeApiService openExchangeApiService;

    @Autowired
    public AccountController(OpenExchangeApiService openExchangeApiService) {
        this.openExchangeApiService = openExchangeApiService;
    }

    //TODO: implement controller (with swagger documentation)
    @GetMapping("/test")
    public Double test() {
        return openExchangeApiService.exchange(100.0, CurrencyCode.USD, CurrencyCode.EUR);
    }
}
