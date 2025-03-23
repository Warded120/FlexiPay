package com.ivan.flexipay.service.payment;

import com.ivan.flexipay.dto.PaymentDto;
import com.ivan.flexipay.entity.Payment;
import com.ivan.flexipay.exception.exceptions.BadRequestException;
import com.ivan.flexipay.exception.exceptions.NotFoundException;

import java.util.List;

/**
 * Service interface for handling payment-related operations.
 */
public interface PaymentService {
    /**
     * Saves {@link Payment} entity to a database
     *
     * @param paymentDto DTO object
     * @author Hrenevych Ivan
     */
    Payment save(PaymentDto paymentDto);

    /**
     * Updates {@link Payment} entity in a database by id
     *
     * @param paymentDto DTO object
     * @author Hrenevych Ivan
     */
    Payment update(Integer paymentId, PaymentDto paymentDto);

    /**
     * Retrieves all payments from the database.
     *
     * @return A list of {@link Payment} entities.
     * @throws NotFoundException If no payments are found.
     */
    List<Payment> getAllPayments();

    /**
     * Processes a payment transaction by transferring funds between accounts.
     *
     * @param paymentId The ID of the payment to process.
     * @return The processed {@link Payment} entity.
     * @throws NotFoundException    If the payment or accounts are not found.
     * @throws BadRequestException If the payment is already processed.
     */
    Payment processPayment(Integer paymentId);

    /**
     * Deletes a payment record by its ID.
     *
     * @param paymentId The ID of the payment to delete.
     * @throws NotFoundException If the payment does not exist.
     */
    void deleteById(Integer paymentId);
}
