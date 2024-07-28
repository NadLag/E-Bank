package org.practice.ebankbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.practice.ebankbackend.enums.AccountStatus;
import org.practice.ebankbackend.enums.Currency;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BankAccount {
   @Id
   private String id;
   private double balance;
   private Date createdAt;
   @Enumerated(EnumType.STRING)
   private Currency currencyType;
   @Enumerated(EnumType.STRING)
   private AccountStatus accountStatus;
   @ManyToOne
   private Customer customer;
   @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
   private List<AccountOperation> accountOperations;

}