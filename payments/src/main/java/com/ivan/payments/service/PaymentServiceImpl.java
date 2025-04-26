package com.ivan.payments.service;

import com.ivan.shared.client.AccountClient;
import com.ivan.shared.client.OpenExchangeClient;
import com.ivan.shared.repo.AccountRepo;
import com.ivan.shared.repo.PaymentRepo;
import com.ivan.shared.dto.PaymentDto;
import com.ivan.shared.entity.Account;
import com.ivan.shared.entity.Currency;
import com.ivan.shared.entity.Payment;
import com.ivan.shared.exception.exceptions.BadRequestException;
import com.ivan.shared.exception.exceptions.NotFoundException;
import com.ivan.shared.mapper.PaymentMapper;
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
    private final OpenExchangeClient openExchangeClient;
    private final AccountClient accountClient;
    private final PaymentMapper paymentMapper;
    private final PaymentRepo paymentRepo;
    private final AccountRepo accountRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment save(PaymentDto paymentDto) {

        Payment payment = paymentMapper.toPayment(paymentDto);

        return paymentRepo.save(payment);
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

        Payment payment = paymentMapper.toPayment(paymentDto);
        payment.setId(paymentId);

        accountRepo.save(payment.getFromAccount());
        accountRepo.save(payment.getToAccount());
        return paymentRepo.save(payment);
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

        Double exchangedAmount = openExchangeClient.exchange(payment.getAmount(), payment.getFromCurrency(), payment.getToCurrency());

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
