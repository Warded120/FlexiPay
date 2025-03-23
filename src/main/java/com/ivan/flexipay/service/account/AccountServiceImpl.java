package com.ivan.flexipay.service.account;

import com.ivan.flexipay.dto.AccountDto;
import com.ivan.flexipay.dto.DepositDto;
import com.ivan.flexipay.entity.Account;
import com.ivan.flexipay.entity.Currency;
import com.ivan.flexipay.exception.exceptions.AccountAlreadyExistsException;
import com.ivan.flexipay.exception.exceptions.NotFoundException;
import com.ivan.flexipay.mapper.AccountMapper;
import com.ivan.flexipay.mapper.CurrencyMapper;
import com.ivan.flexipay.repo.AccountRepo;
import com.ivan.flexipay.repo.CurrencyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing accounts, including financial transactions and deposits.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final CurrencyRepo currencyRepo;
    private final AccountMapper accountMapper;
    private final CurrencyMapper currencyMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        if (accounts.isEmpty()) {
            throw new NotFoundException("No accounts found");
        }
        return accounts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account save(AccountDto accountDto) {
        if (accountRepo.existsById(accountDto.accountId())) {
            throw new AccountAlreadyExistsException("account already exists with account id " + accountDto.accountId());
        }
        Account account = accountMapper.toAccount(accountDto);
        account.setAccountForCurrencies();
        return accountRepo.save(account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account getByAccountId(String accountId) {
        return accountRepo.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id " + accountId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByAccountId(String accountId) {
        if(!accountRepo.existsById(accountId)) {
            throw new NotFoundException("Account not found with accountId: " + accountId);
        }
        accountRepo.deleteById(accountId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account updateById(AccountDto accountDto) {
        if(!accountRepo.existsById(accountDto.accountId())) {
             throw new NotFoundException("Account not found with accountId: " + accountDto.accountId());
        }
        Account account = accountRepo.findByAccountId(accountDto.accountId())
                .orElseThrow(() -> new NotFoundException("Account not found with id " + accountDto.accountId()));;
        account.getCurrencies().clear();

        List<Currency> mappedCurrencies = accountDto.currencies().stream()
                .map(currencyMapper::toCurrency)
                .toList();

        account.getCurrencies().addAll(mappedCurrencies);
        account.setAccountForCurrencies();

        return accountRepo.save(account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency deposit(String accountId, DepositDto depositDto) {

        Account account = accountRepo.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + accountId));
        Currency currency = currencyRepo.findByAccountAndCurrencyCode(account, depositDto.currency())
                .orElseThrow(() -> new NotFoundException(
                        "Currency not found by account id: '" + accountId + "' and/or currency: '" + depositDto.currency() + "'")
                );

        currency.addAmount(depositDto.amount());

        currencyRepo.save(currency);

        return currency;
    }
}
