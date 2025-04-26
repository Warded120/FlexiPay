package com.ivan.shared.repo;

import com.ivan.shared.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.currencies WHERE a.accountId = :accountId")
    Optional<Account> findByAccountId(@Param("accountId") String accountId);
}
