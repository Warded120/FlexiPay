package com.ivan.flexipay.mapper;

import com.ivan.flexipay.dto.AccountDto;
import com.ivan.flexipay.entity.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link AccountDto} and {@link Account} entities.
 * This class uses {@link ModelMapper} to perform the mapping operations.
 */
@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final ModelMapper modelMapper;

    /**
     * Converts the provided {@link AccountDto} to an {@link Account} entity.
     *
     * @param accountDto The {@link AccountDto} to be converted.
     * @return The mapped {@link Account} entity.
     */
    public Account toAccount(AccountDto accountDto) {
        return modelMapper.map(accountDto, Account.class);
    }
}
