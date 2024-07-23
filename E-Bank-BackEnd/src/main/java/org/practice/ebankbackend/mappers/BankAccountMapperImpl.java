package org.practice.ebankbackend.mappers;

import org.practice.ebankbackend.dtos.AccountOperationDTO;
import org.practice.ebankbackend.dtos.CurrentAccountDTO;
import org.practice.ebankbackend.dtos.CustomerDTO;
import org.practice.ebankbackend.dtos.SavingAccountDTO;
import org.practice.ebankbackend.entities.AccountOperation;
import org.practice.ebankbackend.entities.CurrentAccount;
import org.practice.ebankbackend.entities.Customer;
import org.practice.ebankbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

   public CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
      CustomerDTO customerDTO = new CustomerDTO();
      BeanUtils.copyProperties(customer, customerDTO);
      return customerDTO;
   }

   public Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO) {
      Customer customer = new Customer();
      BeanUtils.copyProperties(customerDTO, customer);
      return customer;
   }

   public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount) {
      SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
      BeanUtils.copyProperties(savingAccount, savingAccountDTO);
      savingAccountDTO.setCustomerDTO(convertCustomerToCustomerDTO(savingAccount.getCustomer()));
      savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
      return savingAccountDTO;
   }

   public SavingAccount fromSavingAccountDTO(SavingAccountDTO savingAccountDTO) {
      SavingAccount savingAccount = new SavingAccount();
      BeanUtils.copyProperties(savingAccountDTO, savingAccount);
      savingAccount.setCustomer(convertCustomerDTOToCustomer(savingAccountDTO.getCustomerDTO()));
      return savingAccount;
   }

   public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
      CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
      BeanUtils.copyProperties(currentAccount, currentAccountDTO);
      currentAccountDTO.setCustomerDTO(convertCustomerToCustomerDTO(currentAccount.getCustomer()));
      currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
      return currentAccountDTO;
   }

   public CurrentAccount fromCurrentAccountDTO(CurrentAccountDTO currentAccountDTO) {
      CurrentAccount currentAccount = new CurrentAccount();
      BeanUtils.copyProperties(currentAccountDTO, currentAccount);
      currentAccount.setCustomer(convertCustomerDTOToCustomer(currentAccountDTO.getCustomerDTO()));
      return currentAccount;
   }

   public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
      AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
      BeanUtils.copyProperties(accountOperation, accountOperationDTO);
      return accountOperationDTO;
   }

}
