package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.model.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CancellationRepository extends JpaRepository<Cancellation,Integer> {

    @Query("""
            select c from Cancellation c
            join c.bookings b
            join b.customer cs
            join cs.user u
            where u.username=?1
            """)
    List<Cancellation>allOFCustomer(String name);

}
