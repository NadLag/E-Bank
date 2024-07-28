package org.practice.ebankbackend.repositories;

import org.practice.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
   List<Customer> findByNameContains(String keyword);
   List<Customer> findByNameContainsIgnoreCase(String keyword);
//   public void deleteCustomerById(Long id);

}
