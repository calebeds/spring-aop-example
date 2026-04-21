package me.calebeoliveira.springaopexample.repository;

import me.calebeoliveira.springaopexample.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
