package com.ivan.flexipay.repo;

import com.ivan.flexipay.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, String> {
    Account findByAccountId(String accountId);
}
