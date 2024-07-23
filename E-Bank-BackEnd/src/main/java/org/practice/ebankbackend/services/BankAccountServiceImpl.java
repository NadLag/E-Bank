package org.practice.ebankbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.practice.ebankbackend.dtos.*;
import org.practice.ebankbackend.entities.*;
import org.practice.ebankbackend.enums.OperationType;
import org.practice.ebankbackend.exceptions.BalanceNotEnoughException;
import org.practice.ebankbackend.exceptions.BankAccountNotFoundException;
import org.practice.ebankbackend.exceptions.CustomerNotFoundException;
import org.practice.ebankbackend.mappers.BankAccountMapperImpl;
import org.practice.ebankbackend.repositories.AccountOperationRepository;
import org.practice.ebankbackend.repositories.BankAccountRepository;
import org.practice.ebankbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

   private CustomerRepository customerRepository;
   private BankAccountRepository bankAccountRepository;
   private AccountOperationRepository accountOperationRepository;
   private BankAccountMapperImpl dtoMapper;

   @Override
   public CustomerDTO createCustomer(CustomerDTO customerDTO) {
      log.info("Create new customer");
      Customer customer = dtoMapper.convertCustomerDTOToCustomer(customerDTO);
      Customer createdCustomer = customerRepository.save(customer);
      return dtoMapper.convertCustomerToCustomerDTO(createdCustomer);
   }

   @Override
   public CurrentAccountDTO createCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
      Customer customer = customerRepository.findById(customerId).orElse(null);
      if (customer == null) {
         log.error("Customer not found");
         throw new CustomerNotFoundException("Customer not found");
      }
      CurrentAccount currentAccount = new CurrentAccount();
      currentAccount.setId(UUID.randomUUID().toString());
      currentAccount.setCreatedAt(new Date());
      currentAccount.setBalance(initialBalance);
      currentAccount.setOverdraft(overDraft);
      currentAccount.setCustomer(customer);
      return dtoMapper.fromCurrentAccount(bankAccountRepository.save(currentAccount));
   }

   @Override
   public SavingAccountDTO createSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
      Customer customer = customerRepository.findById(customerId).orElse(null);
      if (customer == null) {
         log.error("Customer not found");
         throw new CustomerNotFoundException("Customer not found");
      }
      SavingAccount savingAccount = new SavingAccount();
      savingAccount.setId(UUID.randomUUID().toString());
      savingAccount.setCreatedAt(new Date());
      savingAccount.setBalance(initialBalance);
      savingAccount.setInterestRate(interestRate);
      savingAccount.setCustomer(customer);
      return dtoMapper.fromSavingAccount(bankAccountRepository.save(savingAccount));
   }

   @Override
   public List<CustomerDTO> customersList() {
      List<Customer> customers = customerRepository.findAll();
      return customers.stream()
              .map(customer -> dtoMapper.convertCustomerToCustomerDTO(customer))
              .collect(Collectors.toList());
   }

   @Override
   public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
      BankAccount bankAccount = bankAccountRepository.findById(accountId)
              .orElseThrow(() -> new BankAccountNotFoundException("Bank Account Not Found"));
      if (bankAccount instanceof SavingAccount) {
         SavingAccount savingAccount = (SavingAccount) bankAccount;
         return dtoMapper.fromSavingAccount(savingAccount);
      } else {
         CurrentAccount currentAccount = (CurrentAccount) bankAccount;
         return dtoMapper.fromCurrentAccount(currentAccount);
      }

   }

   @Override
   public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotEnoughException {
      BankAccount bankAccount = bankAccountRepository.findById(accountId)
              .orElseThrow(() -> new BankAccountNotFoundException("Bank Account Not Found"));
      if (bankAccount.getBalance() < amount) {
         throw new BalanceNotEnoughException("Not enough balance");
      }
      AccountOperation accountOperation = new AccountOperation();
      accountOperation.setOperationType(OperationType.DEBIT);
      accountOperation.setAmount(amount);
      accountOperation.setDescription(description);
      accountOperation.setOperationDate(new Date());
      accountOperation.setBankAccount(bankAccount);
      accountOperationRepository.save(accountOperation);

      bankAccount.setBalance(bankAccount.getBalance() - amount);
      bankAccountRepository.save(bankAccount);
   }

   @Override
   public void credit(String accountId, double amount, String description) throws BalanceNotEnoughException, BankAccountNotFoundException {

      BankAccount bankAccount = bankAccountRepository.findById(accountId)
              .orElseThrow(() -> new BankAccountNotFoundException("Bank Account Not Found"));
      AccountOperation accountOperation = new AccountOperation();
      accountOperation.setOperationType(OperationType.CREDIT);
      accountOperation.setAmount(amount);
      accountOperation.setDescription(description);
      accountOperation.setOperationDate(new Date());
      accountOperation.setBankAccount(bankAccount);
      accountOperationRepository.save(accountOperation);

      bankAccount.setBalance(bankAccount.getBalance() + amount);
      bankAccountRepository.save(bankAccount);
   }

   @Override
   public void transfer(String fromAccountId, String toAccountId, double amount, String description) throws BalanceNotEnoughException, BankAccountNotFoundException {
      debit(fromAccountId, amount, "Transfer to " + toAccountId);
      credit(toAccountId, amount, "Transfer from " + fromAccountId);
   }

   @Override
   public List<BankAccountDTO> getBankAccountsList() {
      List<BankAccount> bankAccounts = (List<BankAccount>) bankAccountRepository.findAll();
      List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(account -> {
         if (account instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) account;
            return dtoMapper.fromSavingAccount(savingAccount);
         } else {
            CurrentAccount currentAccount = (CurrentAccount) account;
            return dtoMapper.fromCurrentAccount(currentAccount);
         }
      }).collect(Collectors.toList());
      return bankAccountDTOS;
   }


   @Override
   public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
      Customer customer = customerRepository.findById(customerId)
              .orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));
      return dtoMapper.convertCustomerToCustomerDTO(customer);
   }

   @Override
   public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
      log.info("Update a customer");
      Customer customer = dtoMapper.convertCustomerDTOToCustomer(customerDTO);
      Customer createdCustomer = customerRepository.save(customer);
      return dtoMapper.convertCustomerToCustomerDTO(createdCustomer);
   }

   @Override
   public void deleteCustomer(Long customerId) {
      customerRepository.deleteById(customerId);
   }

   @Override
   public List<AccountOperationDTO> accountHistory(String accountId) {
      List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
      return accountOperations.stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
   }

   @Override
   public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
      BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
      if (bankAccount == null) throw new BankAccountNotFoundException("Account Not Found");

      Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
      AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
      List<AccountOperationDTO> accountOperationDTOs = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

      accountHistoryDTO.setAccountOperationDTOList(accountOperationDTOs);
      accountHistoryDTO.setAccountId(bankAccount.getId());
      accountHistoryDTO.setBalance(bankAccount.getBalance());
      accountHistoryDTO.setCurrentPage(page);
      accountHistoryDTO.setPageSize(size);
      accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
      return accountHistoryDTO;

   }


}