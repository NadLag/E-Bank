package org.practice.ebankbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.practice.ebankbackend.dtos.AccountOperationDTO;
import org.practice.ebankbackend.dtos.CustomerDTO;
import org.practice.ebankbackend.exceptions.CustomerNotFoundException;
import org.practice.ebankbackend.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {

   private BankAccountService bankAccountService;

   @GetMapping("/customers")
   private List<CustomerDTO> customersList() {
      return bankAccountService.customersList();
   }

   @GetMapping("/customers/search")
   private List<CustomerDTO> searchCustomers(@RequestParam(name ="searchText",defaultValue = "") String keyword) {
      return bankAccountService.searchCustomers(keyword);
   }

   @GetMapping("/customers/{id}")
   public CustomerDTO getCustomerById(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
      return bankAccountService.getCustomer(customerId);
   }

   @PostMapping("/customers")
   public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
      return bankAccountService.createCustomer(customerDTO);
   }

   @PutMapping("/customers/{id}")
   public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
      customerDTO.setId(id);
      return bankAccountService.updateCustomer(customerDTO);
   }

   @DeleteMapping("/customers/{id}")
   public void deleteCustomer(@PathVariable Long id) {
      bankAccountService.deleteCustomer(id);
   }



}