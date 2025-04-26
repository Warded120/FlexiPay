package com.ivan.openexchange.service;

import com.ivan.shared.constant.CurrencyCode;
import com.ivan.shared.exception.exceptions.ExchangeRatesNotAvailableException;

import java.util.Map;

/**
 * Service interface for interacting with currency exchange API's to retrieve and process currency exchange rates.
 */
public interface OpenExchangeApiService {

    /**
     * Retrieves the latest exchange rates from the currency exchange API.
     *
     * @return A map containing currency codes as keys and their corresponding exchange rates as values.
     * @throws ExchangeRatesNotAvailableException If exchange rates cannot be retrieved.
     */
    Map<String, Object> getExchangeRates();

    /**
     * Converts a given amount from one currency to another based on the latest exchange rates.
     *
     * @param amount The amount to be converted.
     * @param from   The source {@link CurrencyCode} currency.
     * @param to     The target {@link CurrencyCode} currency.
     * @return The converted amount in the target currency.
     * @throws ExchangeRatesNotAvailableException If exchange rates for the provided currencies are unavailable.
     */
    Double exchange(Double amount, CurrencyCode from, CurrencyCode to);
}
