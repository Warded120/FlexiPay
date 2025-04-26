package com.ivan.shared.mapper;

import com.ivan.shared.dto.AccountDto;
import com.ivan.shared.dto.CurrencyDto;
import com.ivan.shared.entity.Account;
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
    public AccountDto toAccountDto(Account account) {
        return new AccountDto(
                account.getAccountId(),
                account.getCurrencies().stream()
                        .map(curr ->
                                new CurrencyDto(
                                        curr.getCurrencyCode()
                                )
                        )
                        .toList()
        );
    }
}
