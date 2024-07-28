package org.practice.ebankbackend.services;

import org.practice.ebankbackend.dtos.*;
import org.practice.ebankbackend.entities.BankAccount;
import org.practice.ebankbackend.entities.CurrentAccount;
import org.practice.ebankbackend.entities.Customer;
import org.practice.ebankbackend.entities.SavingAccount;
import org.practice.ebankbackend.exceptions.BalanceNotEnoughException;
import org.practice.ebankbackend.exceptions.BankAccountNotFoundException;
import org.practice.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

   CustomerDTO createCustomer(CustomerDTO customerDTO);

   CurrentAccountDTO createCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

   SavingAccountDTO createSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

   List<CustomerDTO> customersList();

   BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

   void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotEnoughException;

   void credit(String accountId, double amount, String description) throws BalanceNotEnoughException, BankAccountNotFoundException;

   void transfer(String fromAccountId, String toAccountId, double amount, String description) throws BalanceNotEnoughException, BankAccountNotFoundException;

   List<BankAccountDTO> getBankAccountsList();

   CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

   CustomerDTO updateCustomer(CustomerDTO customerDTO);

   void deleteCustomer(Long customerId);

   List<AccountOperationDTO> accountHistory(String accountId);

   AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

   List<CustomerDTO> searchCustomers(String keyword);

}