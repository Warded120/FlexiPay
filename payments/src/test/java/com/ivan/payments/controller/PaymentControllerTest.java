package com.ivan.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.shared.client.OpenExchangeClient;
import com.ivan.shared.constant.CurrencyCode;
import com.ivan.shared.dto.PaymentDto;
import com.ivan.shared.entity.Account;
import com.ivan.shared.entity.Currency;
import com.ivan.shared.entity.Payment;
import com.ivan.shared.repo.AccountRepo;
import com.ivan.shared.repo.CurrencyRepo;
import com.ivan.shared.repo.PaymentRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CurrencyRepo currencyRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @MockitoBean
    private OpenExchangeClient openExchangeClient;

    Payment savedPayment;

    @BeforeEach
    void setUp() {
        // Clear database to ensure test independence
        paymentRepo.deleteAll();
        paymentRepo.flush();
        currencyRepo.deleteAll();
        currencyRepo.flush();
        accountRepo.deleteAll();
        accountRepo.flush();

        Account fromAccount = new Account();
        fromAccount.setAccountId("FROM123");

        Currency fromCurrency = new Currency();
        fromCurrency.setCurrencyCode(CurrencyCode.USD);
        fromCurrency.setAmount(100.0);
        fromCurrency.setAccount(fromAccount);

        fromAccount.setCurrencies(new ArrayList<>());
        fromAccount.getCurrencies().add(fromCurrency);

        Account toAccount = new Account();
        toAccount.setAccountId("TO123");

        Currency toCurrency = new Currency();
        toCurrency.setCurrencyCode(CurrencyCode.EUR);
        toCurrency.setAmount(100.0);
        toCurrency.setAccount(toAccount);

        toAccount.setCurrencies(new ArrayList<>());
        toAccount.getCurrencies().add(toCurrency);

        // Save accounts and currencies
        Account fromSaved = accountRepo.save(fromAccount);
        Account toSaved = accountRepo.save(toAccount);

        // Create test payment
        Payment payment = new Payment();
        payment.setFromAccount(fromSaved);
        payment.setToAccount(toSaved);
        payment.setFromCurrency(CurrencyCode.USD);
        payment.setToCurrency(CurrencyCode.EUR);
        payment.setAmount(50.0);
        payment.setTimestamp(LocalDateTime.now());
        payment.setProcessed(false);

        savedPayment = paymentRepo.save(payment);
    }

    @Test
    void getAllPayments_ReturnsPaymentsList() throws Exception {
        mockMvc.perform(get("/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(savedPayment.getId()))
                .andExpect(jsonPath("$[0].fromAccount.accountId").value("FROM123"))
                .andExpect(jsonPath("$[0].toAccount.accountId").value("TO123"))
                .andExpect(jsonPath("$[0].fromCurrency").value("USD"))
                .andExpect(jsonPath("$[0].toCurrency").value("EUR"))
                .andExpect(jsonPath("$[0].amount").value(50.0))
                .andExpect(jsonPath("$[0].processed").value(false));
    }

    @Test
    void createPayment_ReturnsCreatedPayment() throws Exception {
        PaymentDto newPaymentDto = new PaymentDto("FROM123", "TO123", CurrencyCode.USD, CurrencyCode.EUR, 75.0);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPaymentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fromAccount.accountId").value("FROM123"))
                .andExpect(jsonPath("$.toAccount.accountId").value("TO123"))
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("EUR"))
                .andExpect(jsonPath("$.amount").value(75.0))
                .andExpect(jsonPath("$.processed").value(false));
    }

    @Test
    void updatePayment_ReturnsUpdatedPayment() throws Exception {
        PaymentDto updatedPaymentDto = new PaymentDto("FROM123", "TO123", CurrencyCode.USD, CurrencyCode.EUR, 100.0);

        mockMvc.perform(put("/payments/update/" + savedPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPaymentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedPayment.getId()))
                .andExpect(jsonPath("$.fromAccount.accountId").value("FROM123"))
                .andExpect(jsonPath("$.toAccount.accountId").value("TO123"))
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("EUR"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.processed").value(false));
    }

    @Test
    void processPayment_ReturnsProcessedPayment() throws Exception {
        // Mock exchange rate (50 USD -> 45 EUR, assuming 0.9 rate)
        when(openExchangeClient.exchange(eq(50.0), eq(CurrencyCode.USD), eq(CurrencyCode.EUR)))
                .thenReturn(45.0);

        mockMvc.perform(post("/payments/process/" + savedPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedPayment.getId()))
                .andExpect(jsonPath("$.fromAccount.accountId").value("FROM123"))
                .andExpect(jsonPath("$.toAccount.accountId").value("TO123"))
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("EUR"))
                .andExpect(jsonPath("$.amount").value(50.0))
                .andExpect(jsonPath("$.processed").value(true));

        // Verify account balances
        Account fromAccount = accountRepo.findByAccountId("FROM123").orElseThrow();
        Account toAccount = accountRepo.findByAccountId("TO123").orElseThrow();
        assertEquals(50.0, fromAccount.getCurrency(CurrencyCode.USD).getAmount(), "From account USD balance should be 150.0");
        assertEquals(145.0, toAccount.getCurrency(CurrencyCode.EUR).getAmount(), "To account EUR balance should be 145.0");
    }

    @Test
    void deletePayment_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/payments/" + savedPayment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify database is empty
        assertTrue(paymentRepo.findAll().isEmpty(), "Payments table should be empty after deletion");
        // Verify accounts and currencies are unaffected
        assertTrue(!accountRepo.findAll().isEmpty(), "Accounts table should not be empty");
        assertTrue(!currencyRepo.findAll().isEmpty(), "Currencies table should not be empty");
    }
}