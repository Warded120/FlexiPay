package com.ivan.shared.repo;

import com.ivan.shared.constant.CurrencyCode;
import com.ivan.shared.entity.Account;
import com.ivan.shared.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing {@link Currency} entities.
 */
public interface CurrencyRepo extends JpaRepository<Currency, Integer> {
    /**
     * Finds a {@link Currency} by its associated {@link Account} and currency code.
     *
     * @param account The account associated with the currency.
     * @param currencyCode The currency code to search for.
     * @return An {@link Optional} containing the {@link Currency} if found, or an empty {@link Optional} if not.
     */
    Optional<Currency> findByAccountAndCurrencyCode(Account account, CurrencyCode currencyCode);
}
