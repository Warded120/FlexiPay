package com.ivan.flexipay.mapper;

import com.ivan.flexipay.dto.PaymentDto;
import com.ivan.flexipay.entity.Account;
import com.ivan.flexipay.entity.Payment;
import com.ivan.flexipay.exception.exceptions.NotFoundException;
import com.ivan.flexipay.repo.AccountRepo;
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
        payment.setFromAccount(accountRepo.findByAccountId(paymentDto.fromAccount())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + paymentDto.fromAccount())));
        payment.setToAccount(accountRepo.findByAccountId(paymentDto.toAccount())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + paymentDto.toAccount())));
        payment.setTimestamp(LocalDateTime.now());

        return payment;
    }
}

