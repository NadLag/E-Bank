package org.practice.ebankbackend;

import org.practice.ebankbackend.dtos.BankAccountDTO;
import org.practice.ebankbackend.dtos.CurrentAccountDTO;
import org.practice.ebankbackend.dtos.CustomerDTO;
import org.practice.ebankbackend.dtos.SavingAccountDTO;
import org.practice.ebankbackend.entities.*;
import org.practice.ebankbackend.enums.AccountStatus;
import org.practice.ebankbackend.enums.Currency;
import org.practice.ebankbackend.enums.OperationType;
import org.practice.ebankbackend.exceptions.BalanceNotEnoughException;
import org.practice.ebankbackend.exceptions.BankAccountNotFoundException;
import org.practice.ebankbackend.exceptions.CustomerNotFoundException;
import org.practice.ebankbackend.repositories.AccountOperationRepository;
import org.practice.ebankbackend.repositories.BankAccountRepository;
import org.practice.ebankbackend.repositories.CustomerRepository;
import org.practice.ebankbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankBackEndApplication {

   public static void main(String[] args) {
      SpringApplication.run(EBankBackEndApplication.class, args);
   }

   @Bean
   CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
      return args -> {
         Stream.of("Nadir", "Hajar", "Youssef", "Anis", "Maya").forEach(name -> {

            CustomerDTO customer = new CustomerDTO();
            customer.setName(name);
            customer.setEmail(name + "@gmail.com");
            bankAccountService.createCustomer(customer);
         });
         bankAccountService.customersList().forEach(customer -> {
            try {
               bankAccountService.createCurrentAccount(Math.random() + 90000, 100000, customer.getId());
               bankAccountService.createSavingAccount(Math.random() + 120000, 4.89, customer.getId());

            } catch (CustomerNotFoundException e) {
               throw new RuntimeException(e);
            }
         });
         List<BankAccountDTO> bankAccounts = bankAccountService.getBankAccountsList();
         for (BankAccountDTO bankAccount : bankAccounts) {
            for (int i = 0; i < 10; i++) {
               String accountId;
               if (bankAccount instanceof SavingAccountDTO) {
                  accountId = ((SavingAccountDTO) bankAccount).getId();
               } else {
                  accountId = ((CurrentAccountDTO) bankAccount).getId();
               }
               bankAccountService.credit(accountId, 10000 + Math.random() * 120000, "Credit");
               bankAccountService.debit(accountId, 1000 + Math.random() * 12000, "Debit");
            }
         }
      };
   }


   //   @Bean
   CommandLineRunner start(CustomerRepository customerRepository,
                           BankAccountRepository bankAccountRepository,
                           AccountOperationRepository accountOperationRepository) {

      return args -> {
         Stream.of("Nadir", "Hajar", "Youssef", "Anis").forEach(name -> {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(name + "@gmail.com");
            customerRepository.save(customer);
         });

         customerRepository.findAll().forEach(customer -> {
            // Create a Current Account for each Customer
            CurrentAccount currentAccount = new CurrentAccount();
            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setBalance(Math.random() * 90000);
            currentAccount.setCurrencyType(Math.random() > 0.5 ? Currency.MAD : Currency.USD);
            currentAccount.setCreatedAt(new Date());
            currentAccount.setAccountStatus(AccountStatus.CREATED);
            currentAccount.setCustomer(customer);
            currentAccount.setOverdraft(9000);
            bankAccountRepository.save(currentAccount);

            // Create a Saving Account for each Customer
            SavingAccount savingAccount = new SavingAccount();
            savingAccount.setId(UUID.randomUUID().toString());
            savingAccount.setBalance(Math.random() * 90000);
            savingAccount.setCurrencyType(Math.random() > 0.5 ? Currency.EUR : Currency.JPY);
            savingAccount.setCreatedAt(new Date());
            savingAccount.setAccountStatus(AccountStatus.CREATED);
            savingAccount.setCustomer(customer);
            savingAccount.setInterestRate(5.14);
            bankAccountRepository.save(savingAccount);

         });

         bankAccountRepository.findAll().forEach(bankAccount -> {
            for (int i = 0; i < 10; i++) {
               AccountOperation accountOperation = new AccountOperation();
               accountOperation.setOperationDate(new Date());
               accountOperation.setAmount(Math.random() * 120000);
               accountOperation.setOperationType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
               accountOperation.setBankAccount(bankAccount);
               accountOperationRepository.save(accountOperation);
            }
         });

      };
   }

}
