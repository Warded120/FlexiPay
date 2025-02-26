package com.ivan.flexipay.repo;

import com.ivan.flexipay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    //TODO: create repo methods
}
