package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.model.Coupons;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupons, Integer> {

    @Query("""
            select c from Coupons c
            where c.code=?1
            """)
    Coupons getByCouponCode(String s);
}
