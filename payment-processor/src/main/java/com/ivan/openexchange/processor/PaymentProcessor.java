package com.ivan.openexchange.processor;

import com.ivan.openexchange.sink.PaymentSink;
import com.ivan.shared.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class PaymentProcessor {
    private final PaymentSink paymentSink;

    public Consumer<Payment> process() {
        return paymentSink::sink;
    }
}
