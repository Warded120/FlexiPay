package com.ivan.openexchange.sink;

import com.ivan.openexchange.service.OpenExchangeApiService;
import com.ivan.shared.entity.Account;
import com.ivan.shared.entity.Currency;
import com.ivan.shared.entity.Payment;
import com.ivan.shared.exception.exceptions.NotFoundException;
import com.ivan.shared.repo.AccountRepo;
import com.ivan.shared.repo.CurrencyRepo;
import com.ivan.shared.repo.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentSink {
    private final OpenExchangeApiService openExchangeApiService;
    private final AccountRepo accountRepo;
    private final PaymentRepo paymentRepo;
    private final CurrencyRepo currencyRepo;

    //TODO: maybe test it
    public void sink(Payment payment) {
        System.out.println("sinking payment: " + payment);
        if (payment.isProcessed()) {
            return;
        }

        Payment dbPayment = paymentRepo.findById(payment.getId())
                .orElseThrow(() -> new NotFoundException("payment not found with id: " + payment.getId()));

        Account fromAccount = accountRepo.findByAccountId(dbPayment.getFromAccount().getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + dbPayment.getFromAccount().getAccountId()));

        Account toAccount = accountRepo.findByAccountId(dbPayment.getToAccount().getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found by id: " + dbPayment.getToAccount().getAccountId()));

        Double exchangedAmount = openExchangeApiService.exchange(dbPayment.getAmount(), dbPayment.getFromCurrency(), dbPayment.getToCurrency());

        Currency fromAccountCurrency = fromAccount.getCurrency(dbPayment.getFromCurrency());
        Currency toAccountCurrency = toAccount.getCurrency(dbPayment.getToCurrency());

        fromAccountCurrency.setAmount(fromAccountCurrency.getAmount() - dbPayment.getAmount());
        toAccountCurrency.setAmount(toAccountCurrency.getAmount() + exchangedAmount);

        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);

        dbPayment.setProcessed(true);
        dbPayment.setTimestamp(LocalDateTime.now());

        paymentRepo.save(dbPayment);
    }
}
