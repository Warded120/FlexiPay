package com.ivan.flexipay.repo;

import com.ivan.flexipay.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing {@link Account} entities.
 */
public interface AccountRepo extends JpaRepository<Account, String> {

    /**
     * Finds an {@link Account} by its account ID.
     *
     * @param accountId The unique identifier for the account.
     * @return An {@link Optional} containing the {@link Account} if found, or an empty {@link Optional} if not.
     */
    Optional<Account> findByAccountId(String accountId);
}
