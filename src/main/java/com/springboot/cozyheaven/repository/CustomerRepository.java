package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    @Query("""
           select c from Customer c
           join c.user u
           where u.username=?1
           """)
    Customer getCustomerByUsername(String name);

}
