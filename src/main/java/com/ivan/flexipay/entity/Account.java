package com.ivan.flexipay.entity;

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

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Currency> currencies;

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
