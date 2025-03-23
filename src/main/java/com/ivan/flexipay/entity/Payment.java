package com.ivan.flexipay.entity;

import com.ivan.flexipay.constant.CurrencyCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "from_currency")
    @Enumerated(EnumType.STRING)
    private CurrencyCode fromCurrency;

    @Column(name = "to_currency")
    @Enumerated(EnumType.STRING)
    private CurrencyCode toCurrency;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "is_processed")
    private boolean isProcessed = Boolean.FALSE;

    public static final String defaultJson = """
                {
                  "id": 0,
                  "fromAccount": {
                    "accountId": "string",
                    "currencies": [
                      {
                        "id": 0,
                        "currencyCode": "USD",
                        "amount": 0
                      }
                    ]
                  },
                  "toAccount": {
                    "accountId": "string",
                    "currencies": [
                      {
                        "id": 0,
                        "currencyCode": "USD",
                        "amount": 0
                      }
                    ]
                  },
                  "amount": 0,
                  "fromCurrency": "USD",
                  "toCurrency": "USD",
                  "timestamp": "2025-01-01T00:00:00.000Z",
                  "processed": false
                }
            """;
}
