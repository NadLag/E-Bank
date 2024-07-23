package org.practice.ebankbackend.dtos;

import lombok.Data;
import org.practice.ebankbackend.enums.AccountStatus;
import org.practice.ebankbackend.enums.Currency;

import java.util.Date;

@Data
public class SavingAccountDTO extends BankAccountDTO {

   private String id;
   private double balance;
   private Date createdAt;
   private Currency currencyType;
   private AccountStatus accountStatus;
   private CustomerDTO customerDTO;
   private double interestRate;

}