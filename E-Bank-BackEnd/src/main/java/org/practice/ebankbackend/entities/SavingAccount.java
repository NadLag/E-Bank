package org.practice.ebankbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("SAV")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingAccount extends BankAccount {
   private double interestRate;
}
