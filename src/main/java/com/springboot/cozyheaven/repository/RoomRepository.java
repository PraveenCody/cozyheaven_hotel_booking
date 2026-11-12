package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.dto.response.RoomRespDto;
import com.springboot.cozyheaven.dto.response.RoomStatusDto;
import com.springboot.cozyheaven.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.springboot.cozyheaven.dto.response.RoomRespDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Integer> {

    @Query("""
            select r from Room r
            join r.hotel h
            join h.hotelOwner hr
            join hr.user u
            where u.username=?1
            """)
    List<Room> getAllByOwner(String name);



    @Query("""
    SELECT
        r.id,
        h.hotelName,
        h.description,
        h.location,
        h.city,
        r.roomType,
        r.bedType,
        r.roomSize,
        r.basePrice,
        r.conditioning,
        h.overAllRating,
        h.imgUrl
    FROM Room r
    JOIN r.hotel h
    WHERE r.maxGuest >= (?4 + ?5)
      AND LOWER(h.city) LIKE LOWER(CONCAT('%', ?1, '%'))
      AND NOT EXISTS (
          SELECT b
          FROM Bookings b
          WHERE b.room = r
            AND b.status <> com.springboot.cozyheaven.enums.BookingStatus.CANCELLED
            AND b.checkIn < ?3
            AND b.checkOut > ?2
      )
    """)
    List<RoomRespDto> dynamicSearch(String city,LocalDateTime checkIn,LocalDateTime checkOut,int noOfAdults, int noOfChildren);

    @Query("""
            select r from Room r
            join r.hotel h
            where h.hotelId=?1
            """)
    List<Room> getAllByHotel(int id);

    @Query("""
            select r.status as roomStatus, count(r.id) as noOfRooms from Room r
            group by r.status
            """)
    List<RoomStatusDto> chartAdmin();
}
