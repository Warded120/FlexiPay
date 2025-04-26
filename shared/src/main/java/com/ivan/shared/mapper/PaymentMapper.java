package com.ivan.shared.mapper;

import com.ivan.shared.client.AccountClient;
import com.ivan.shared.dto.PaymentDto;
import com.ivan.shared.entity.Account;
import com.ivan.shared.entity.Currency;
import com.ivan.shared.entity.Payment;
import com.ivan.shared.exception.exceptions.NotFoundException;
import com.ivan.shared.repo.AccountRepo;
import com.ivan.shared.repo.CurrencyRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Mapper class for converting between {@link PaymentDto} and {@link Payment} entities.
 * This class uses {@link ModelMapper} for basic mapping and performs additional operations
 * like setting accounts from the repository and setting a timestamp.
 */
@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final ModelMapper modelMapper;
    private final CurrencyRepo currencyRepo;
    private final AccountRepo accountRepo;

    /**
     * Converts the provided {@link PaymentDto} to a {@link Payment} entity.
     * Additionally, it fetches the {@link Account} entities for the 'from' and 'to' accounts
     * and sets them in the {@link Payment}. It also sets the current timestamp for the payment.
     *
     * @param paymentDto The {@link PaymentDto} to be converted.
     * @return The mapped {@link Payment} entity.
     * @throws NotFoundException if any of the accounts specified in the payment DTO are not found.
     */
    public Payment toPayment(PaymentDto paymentDto) {
        Payment payment = modelMapper.map(paymentDto, Payment.class);

        Account fromAccount = accountRepo.findByAccountId(paymentDto.fromAccount())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + paymentDto.fromAccount()));

        Account toAccount = accountRepo.findByAccountId(paymentDto.toAccount())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + paymentDto.toAccount()));

        Currency fromCurrency = currencyRepo.findById(fromAccount.getCurrency(paymentDto.fromCurrency()).getId())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + paymentDto.fromAccount()));

        Currency toCurrency = currencyRepo.findById(toAccount.getCurrency(paymentDto.toCurrency()).getId())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + paymentDto.fromAccount()));

        payment.setFromAccount(fromAccount);
        payment.setFromCurrency(fromCurrency.getCurrencyCode());
        payment.setToAccount(toAccount);
        payment.setToCurrency(toCurrency.getCurrencyCode());
        payment.setTimestamp(LocalDateTime.now());

        return payment;
    }
}

