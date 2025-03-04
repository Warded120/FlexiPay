package com.ivan.flexipay.service.account;

import com.ivan.flexipay.dto.CurrencyDto;
import com.ivan.flexipay.dto.account.AccountDto;
import com.ivan.flexipay.entity.Account;
import com.ivan.flexipay.entity.Currency;
import com.ivan.flexipay.exception.exceptions.AccountAlreadyExistsException;
import com.ivan.flexipay.exception.exceptions.NotFoundException;
import com.ivan.flexipay.repo.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepo repo;
    private final ModelMapper modelMapper;

    @Override
    public Account save(AccountDto accountDto) {
        if (repo.existsById(accountDto.accountId())) {
            throw new AccountAlreadyExistsException("account already exists with account id " + accountDto.accountId());
        }
        Account account = modelMapper.map(accountDto, Account.class);
        account.setAccountForCurrencies();
        return repo.save(account);
    }

    @Override
    public Account getByAccountId(String accountId) {
        if(repo.existsById(accountId)) {
            return repo.findByAccountId(accountId);
        }
        throw new NotFoundException("Account not found with id " + accountId);
    }

    @Override
    public void deleteByAccountId(String accountId) {
        if(repo.existsById(accountId)) {
            repo.deleteById(accountId);
        } else {
            throw new NotFoundException("Account not found with accountId: " + accountId);
        }
    }

    @Override
    public Account updateById(String accountId, List<CurrencyDto> currencies) {
        if(!repo.existsById(accountId)) {
             throw new NotFoundException("Account not found with accountId: " + accountId);
        }
        Account account = repo.findByAccountId(accountId);

        List<Currency> mappedCurrencies = currencies.stream()
                .map(currencyDto -> modelMapper.map(currencyDto, Currency.class))
                .toList();

        account.setCurrencies(new ArrayList<>(mappedCurrencies));
        account.setAccountForCurrencies();

        return repo.save(account);
    }
}
