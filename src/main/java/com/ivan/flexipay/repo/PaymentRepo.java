package com.ivan.flexipay.repo;

import com.ivan.flexipay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing {@link Payment} entities.
 */
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
}
