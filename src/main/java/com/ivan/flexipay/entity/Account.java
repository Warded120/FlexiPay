package com.ivan.flexipay.entity;

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
@ToString
public class Account {
    @Id @Column(name = "account_number")
    private String accountNumber;

    @OneToMany(mappedBy = "account")
    private List<Currency> currencies;
}
