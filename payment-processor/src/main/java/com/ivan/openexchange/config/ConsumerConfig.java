package com.ivan.openexchange.config;

import com.ivan.openexchange.processor.PaymentProcessor;
import com.ivan.shared.entity.Payment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ConsumerConfig {
    @Bean
    public Consumer<Payment> processPayment(PaymentProcessor paymentProcessor) {
        return paymentProcessor.process();
    }
}
