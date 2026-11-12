package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.dto.response.BookingsStatusDto;
import com.springboot.cozyheaven.dto.response.RoomStatusDto;
import com.springboot.cozyheaven.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingsRepository extends JpaRepository<Bookings,Integer> {


    @Query("""
            select b from Bookings b
            join b.customer c
            join c.user u
            where u.username=?1
            """)
    List<Bookings> findAllByUsername(String name);

    @Query("""
            select b from Bookings b
            join b.room r
            join r.hotel h
            where h.id=?1
            """)
    List<Bookings> getBookingsByHotelId(int id);


    @Query("""
            select b from Bookings b
            join b.room r
            join r.hotel h
            join h.hotelOwner ho
            where ho.id=?1
            """)
    List<Bookings> getBookingsByOwnerID(int id);



    @Query("""
            select b.status as bookingStatus, count(b.id) as noOfBookings from Bookings b
            group by b.status
            """)
    List<BookingsStatusDto> adminDonut();

    @Query("""
            select b.status as bookingStatus, count(b.id) as noOfBookings from Bookings b
            join b.room r
            join r.hotel h
            join h.hotelOwner ho
            join ho.user u
            where u.username=?1
            group by b.status
            """)
    List<BookingsStatusDto> donut(String name);

    @Query("""
    SELECT COUNT(b) > 0
    FROM Bookings b
    WHERE b.room.id = ?1
    AND b.status <> 'CANCELLED'
    AND b.checkIn < ?3
    AND b.checkOut > ?2
    """)
    boolean isRoomAlreadyBooked(int roomId, LocalDateTime checkIn, LocalDateTime checkOut);
}
