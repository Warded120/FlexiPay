package com.ivan.flexipay.service.payment;

import com.ivan.flexipay.dto.PaymentDto;
import com.ivan.flexipay.entity.Account;
import com.ivan.flexipay.entity.Currency;
import com.ivan.flexipay.entity.Payment;
import com.ivan.flexipay.exception.exceptions.BadRequestException;
import com.ivan.flexipay.exception.exceptions.NotFoundException;
import com.ivan.flexipay.mapper.PaymentMapper;
import com.ivan.flexipay.repo.AccountRepo;
import com.ivan.flexipay.repo.PaymentRepo;
import com.ivan.flexipay.service.openExchangeApi.OpenExchangeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for handling payment-related operations.
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OpenExchangeApiService openExchangeApiService;
    private final PaymentMapper paymentMapper;
    private final AccountRepo accountRepo;
    private final PaymentRepo paymentRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment save(PaymentDto paymentDto) {
        Payment payment = paymentMapper.toPayment(paymentDto);

        paymentRepo.save(payment);

        return payment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Payment> getAllPayments() {
        List<Payment> payments = paymentRepo.findAll();
        if(payments.isEmpty()) {
            throw new NotFoundException("No payments found");
        }
        return payments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment update(Integer paymentId, PaymentDto paymentDto) {

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found by id: " + paymentId));

        payment.setFromAccount(accountRepo.findByAccountId(paymentDto.fromAccount())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + paymentDto.fromAccount())));
        payment.setToAccount(accountRepo.findByAccountId(paymentDto.toAccount())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + paymentDto.toAccount())));
        payment.setFromCurrency(paymentDto.fromCurrency());
        payment.setToCurrency(paymentDto.toCurrency());
        payment.setAmount(paymentDto.amount());
        payment.setTimestamp(LocalDateTime.now());

        return payment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment processPayment(Integer paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found by id: " + paymentId));

        if (payment.isProcessed()) {
            throw new BadRequestException("Payment is already processed");
        }

        Account fromAccount = accountRepo.findByAccountId(payment.getFromAccount().getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + payment.getFromAccount().getAccountId()));

        Account toAccount = accountRepo.findByAccountId(payment.getToAccount().getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + payment.getToAccount().getAccountId()));

        Double exchangedAmount = openExchangeApiService.exchange(payment.getAmount(), payment.getFromCurrency(), payment.getToCurrency());

        Currency fromAccountCurrency = fromAccount.getCurrency(payment.getFromCurrency());
        Currency toAccountCurrency = toAccount.getCurrency(payment.getToCurrency());

        fromAccountCurrency.setAmount(fromAccountCurrency.getAmount() - payment.getAmount());
        toAccountCurrency.setAmount(toAccountCurrency.getAmount() + exchangedAmount);

        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);

        payment.setProcessed(true);
        payment.setTimestamp(LocalDateTime.now());

        paymentRepo.save(payment);

        return payment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Integer paymentId) {
        if(!paymentRepo.existsById(paymentId)) {
            throw new NotFoundException("Payment not found by id: " + paymentId);
        }
        paymentRepo.deleteById(paymentId);
    }
}
