package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.dto.response.HotelRespDto;
import com.springboot.cozyheaven.dto.response.HotelStatusDto;
import com.springboot.cozyheaven.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer> {

    @Query("""
            select h from Hotel h
            where h.hotelName=?1
            """)
    Hotel getByName(String name);

    @Query("""
            select h from Hotel h
            join h.hotelOwner ho
            join ho.user u
            where u.username=?1
            """)
    List<Hotel> findAllByUsername(String name);


    @Query("""
            select h.status as hotelStatus,count(h.hotelId) as noOfHotels from Hotel h
            group by h.status
            """)
    List<HotelStatusDto> chartAdmin();
}
