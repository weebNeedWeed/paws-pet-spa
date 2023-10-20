package com.paws.persistence.repositories;

import com.paws.persistence.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.username = :username")
    Customer getCustomerByUsername(String username);

    @Query("select case when count(1) = 1 then true else false end from Customer c where c.username = :username")
    boolean existsCustomerWithUsername(String username);
}
