package com.ivan.payments.service;

import com.ivan.shared.exception.exceptions.BadRequestException;
import com.ivan.shared.repo.AccountRepo;
import com.ivan.shared.repo.PaymentRepo;
import com.ivan.shared.dto.PaymentDto;
import com.ivan.shared.entity.Payment;
import com.ivan.shared.exception.exceptions.NotFoundException;
import com.ivan.shared.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service implementation for handling payment-related operations.
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public static final String SUCCESS = "Payment will be processed shortly";
    public static final String FAILURE = "Error during processing payment";

    private final StreamBridge streamBridge;
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
    public String processPayment(Integer paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found by id: " + paymentId));

        if (payment.isProcessed()) {
            throw new BadRequestException("Payment is already processed");
        }

        return streamBridge.send("processPayment-out-0", payment)
                ? SUCCESS
                : FAILURE;
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
