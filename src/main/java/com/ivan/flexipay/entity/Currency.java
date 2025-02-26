package com.ivan.flexipay.entity;

import com.ivan.flexipay.constant.CurrencyCode;
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

@Entity
@Table(name = "currencies")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Currency {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode code;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
