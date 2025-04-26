package com.ivan.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.shared.constant.CurrencyCode;
import com.ivan.shared.dto.AccountDto;
import com.ivan.shared.dto.CurrencyDto;
import com.ivan.shared.dto.DepositDto;
import com.ivan.shared.entity.Account;
import com.ivan.shared.entity.Currency;
import com.ivan.shared.repo.AccountRepo;
import com.ivan.shared.repo.CurrencyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CurrencyRepo currencyRepo;

    private AccountDto accountDto;
    private DepositDto depositDto;

    @BeforeEach
    void setUp() {
        currencyRepo.deleteAll();
        accountRepo.deleteAll();

        Account account = new Account();
        account.setAccountId("TEST123");

        Currency currency = new Currency();
        currency.setCurrencyCode(CurrencyCode.USD);
        currency.setAmount(100.0);
        currency.setAccount(account);

        account.setCurrencies(new ArrayList<>());
        account.getCurrencies().add(currency);

        accountRepo.save(account);

        accountDto = new AccountDto("NEW123", List.of(new CurrencyDto(CurrencyCode.EUR)));
        depositDto = new DepositDto(CurrencyCode.USD, 50.0);
    }

    @Test
    void getAllAccounts_ReturnsAccountsList() throws Exception {
        mockMvc.perform(get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value("TEST123"))
                .andExpect(jsonPath("$[0].currencies[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$[0].currencies[0].amount").value(100.0));
    }

    @Test
    void createAccount_ReturnsCreatedAccount() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value("NEW123"))
                .andExpect(jsonPath("$.currencies[0].currencyCode").value("EUR"))
                .andExpect(jsonPath("$.currencies[0].amount").value(0.0));
    }

    @Test
    void getAccountById_ReturnsAccount() throws Exception {
        mockMvc.perform(get("/accounts/TEST123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("TEST123"))
                .andExpect(jsonPath("$.currencies[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$.currencies[0].amount").value(100.0));
    }

    @Test
    void updateAccount_ReturnsUpdatedAccount() throws Exception {
        AccountDto updatedAccount = new AccountDto("TEST123",
                List.of(new CurrencyDto(CurrencyCode.EUR)));

        mockMvc.perform(put("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value("TEST123"))
                .andExpect(jsonPath("$.currencies[0].currencyCode").value("EUR"))
                .andExpect(jsonPath("$.currencies[0].amount").value(0.0));
    }

    @Test
    void deleteAccount_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/accounts/TEST123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertTrue(accountRepo.findAll().isEmpty(), "Accounts should be empty");
        assertTrue(currencyRepo.findAll().isEmpty(), "Currencies should be empty");
    }

    @Test
    void depositToAccount_ReturnsUpdatedCurrency() throws Exception {
        mockMvc.perform(post("/accounts/deposit-to/TEST123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.amount").value(150.0));
    }
}