package org.practice.ebankbackend.repositories;

import org.practice.ebankbackend.entities.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends CrudRepository<BankAccount, String> {
}
