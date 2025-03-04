package com.ivan.flexipay.service.account;

import com.ivan.flexipay.dto.CurrencyDto;
import com.ivan.flexipay.dto.account.AccountDto;
import com.ivan.flexipay.entity.Account;
import jakarta.validation.Valid;

import java.util.List;

public interface AccountService {
    Account save(AccountDto accountDto);
    Account getByAccountId(String accountNumber);
    void deleteByAccountId(String accountId);
    Account updateById(String accountId, List<CurrencyDto> currencies);
}
