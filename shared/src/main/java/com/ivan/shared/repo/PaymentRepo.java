package com.ivan.shared.repo;

import com.ivan.shared.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing {@link Payment} entities.
 */
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
}
