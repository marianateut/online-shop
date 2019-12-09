package org.fasttrackit.onlineshop.persisteance;

import org.fasttrackit.onlineshop.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
