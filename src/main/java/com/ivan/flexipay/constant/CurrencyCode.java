package com.ivan.flexipay.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CurrencyCode {
    EUR,
    GBP,
    CHF,
    SEK,
    NOK,
    DKK,
    PLN,
    HUF,
    CZK,
    BGN,
    RON,
    HRK,
    ISK,
    MDL,
    RSD,
    MKD,
    UAH,
    KZT,
    USD,
    CAD,
    MXN,
    ARS,
    BRL,
    COP,
    PEN,
    CLP,
    PYG,
    BOB,
    SVC,
    GYD,
    CNY,
    JPY,
    INR,
    ZAR;

    @JsonCreator
    public static CurrencyCode fromString(String value) {
        if (value != null) {
            for (CurrencyCode currencyCode : CurrencyCode.values()) {
                if (currencyCode.name().equalsIgnoreCase(value)) {
                    return currencyCode;
                }
            }
        }
        return null;
    }

    @JsonValue
    public String toJson() {
        return this.name();
    }
}
