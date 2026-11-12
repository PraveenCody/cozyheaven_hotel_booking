package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {


    @Query("""
            select h from Bookings b
            join b.room r
            join r.hotel h
            where b.id=?1
            """)
    Hotel getHotelIdByBookingId(int bid);

    @Query("""
            select r from Review r
            join r.customer c
            join c.user u
            where u.username=?1
            """)
    List<Review> getAllReviewByCustomer(String name);

    @Query("""
            select r from Review r
            join r.hotel h
            join h.hotelOwner ho
            join ho.user u
            where u.username=?1
            """)
    List<Review> getAllReviewByHotel(String name);

    @Query("""
            select count(r.reviewId) from Review r
            join r.hotel h
            where h.hotelId=?1
            """)
    Integer getCountOfHotelReview(int hid);
}
