package com.ivan.flexipay.mapper;

import com.ivan.flexipay.dto.CurrencyDto;
import com.ivan.flexipay.entity.Currency;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link CurrencyDto} and {@link Currency} entities.
 * This class uses {@link ModelMapper} to perform the mapping operations.
 */
@Component
@RequiredArgsConstructor
public class CurrencyMapper {

    private final ModelMapper modelMapper;

    /**
     * Converts the provided {@link CurrencyDto} to a {@link Currency} entity.
     *
     * @param currencyDto The {@link CurrencyDto} to be converted.
     * @return The mapped {@link Currency} entity.
     */
    public Currency toCurrency(CurrencyDto currencyDto) {
        return modelMapper.map(currencyDto, Currency.class);
    }
}

