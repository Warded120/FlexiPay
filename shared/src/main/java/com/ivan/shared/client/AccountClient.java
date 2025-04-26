package com.ivan.shared.client;

import com.ivan.shared.dto.AccountDto;
import com.ivan.shared.entity.Account;
import com.ivan.shared.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountClient {

    private final RestTemplate restTemplate;
    private final AccountMapper accountMapper;

    private static final String GET_ACCOUNT_BY_ID = "http://localhost:8081/accounts/{accountId}";
    private static final String SAVE_ACCOUNT = "http://localhost:8081/accounts";

    //TODO: test and debug
    public Optional<Account> fetchAccountById(String accountId) {
        return Optional.ofNullable(
                restTemplate.getForObject(GET_ACCOUNT_BY_ID, Account.class, accountId)
        );
    }

    //TODO: test and debug
    public Account saveAccount(Account account) {
        AccountDto accountDto = accountMapper.toAccountDto(account);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountDto> request = new HttpEntity<>(accountDto, headers);

        ResponseEntity<Account> response = restTemplate.postForEntity(SAVE_ACCOUNT, request, Account.class);
        return response.getBody();
    }
}