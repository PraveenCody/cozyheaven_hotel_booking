package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.dto.response.PaymentStatusDto;
import com.springboot.cozyheaven.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {

    @Query("""
            select p from Payment p
            join p.booking b
            where b.id=?1
            """)
    Payment getByBookingId(int id);


    @Query("""
            select p.paymentStatus as status,count(b.id) as noOfBookings from Payment p
            join p.booking b
            join b.room r
            join r.hotel h
            join h.hotelOwner ho
            join ho.user u
            where u.username=?1
            group by p.paymentStatus
            """)
    List<PaymentStatusDto> chart(String name);

    @Query("""
            select p.paymentStatus as status,count(b.id) as noOfBookings from Payment p
            join p.booking b
            group by p.paymentStatus
            """)
    List<PaymentStatusDto> chartAdmin();

    @Query("""
            select p from Payment p
            join p.booking b
            join b.customer c
            join c.user u
            where u.username=?1
            """)
    List<Payment> myPayments(String name);
}
