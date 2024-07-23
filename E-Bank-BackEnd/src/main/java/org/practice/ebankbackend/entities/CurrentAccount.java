package org.practice.ebankbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CUR")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAccount extends BankAccount {
   private double overdraft;

}
