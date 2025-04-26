package com.ivan.accounts.service;

import com.ivan.shared.dto.AccountDto;
import com.ivan.shared.dto.DepositDto;
import com.ivan.shared.entity.Account;
import com.ivan.shared.entity.Currency;
import com.ivan.shared.exception.exceptions.AccountAlreadyExistsException;
import com.ivan.shared.exception.exceptions.NotFoundException;

import java.util.List;

/**
 * Service interface for managing accounts, including financial transactions and deposits.
 */
public interface AccountService {
    /**
     * Retrieves all accounts from the repository.
     *
     * @return A list of all accounts.
     * @throws NotFoundException If no accounts are found.
     */
    List<Account> getAllAccounts();

    /**
     * Saves a new account to the repository.
     *
     * @param accountDto The data transfer object containing account details.
     * @return The saved account.
     * @throws AccountAlreadyExistsException If an account with the given account ID already exists.
     */
    Account save(AccountDto accountDto);

    /**
     * Retrieves an account by its account ID.
     *
     * @param accountId The ID of the account to retrieve.
     * @return The account corresponding to the given account ID.
     * @throws NotFoundException If no account is found with the given account ID.
     */
    Account getByAccountId(String accountId);

    /**
     * Deletes an account by its account ID.
     *
     * @param accountId The ID of the account to delete.
     * @throws NotFoundException If no account is found with the given account ID.
     */
    void deleteByAccountId(String accountId);

    /**
     * Updates an account with new data provided in the account DTO.
     *
     * @param accountDto The data transfer object containing updated account details.
     * @return The updated account.
     * @throws NotFoundException If no account is found with the given account ID.
     */
    Account updateById(AccountDto accountDto);

    /**
     * Deposits a specified amount of currency into an account.
     *
     * @param accountId  The ID of the account to deposit into.
     * @param depositDto The deposit details, including currency type and amount.
     * @return The updated currency object after deposit.
     * @throws NotFoundException If the account or currency is not found.
     */
    Currency deposit(String accountId, DepositDto depositDto);

}
