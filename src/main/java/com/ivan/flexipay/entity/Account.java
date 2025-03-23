package com.ivan.flexipay.entity;

import com.ivan.flexipay.constant.CurrencyCode;
import com.ivan.flexipay.exception.exceptions.NotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "currencies")
public class Account {
    @Id @Column(name = "account_number")
    private String accountId;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Currency> currencies;

    public Currency getCurrency(CurrencyCode code) {
        for (Currency currency : currencies) {
            if (currency.getCurrencyCode().equals(code)) {
                return currency;
            }
        }
        throw new NotFoundException("Currency not found by code: '" + code + "' in account: '" + accountId + "'");
    }

    public void setAccountForCurrencies() {
        currencies.forEach(currency -> currency.setAccount(this));
    }

    public static final String defaultJson = """
            {
              "accountId": "string",
              "currencies": [
                {
                  "currencyCode": "USD"
                }
              ]
            }
            """;
}
