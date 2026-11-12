package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.dto.response.HotelOwnerRespDto;
import com.springboot.cozyheaven.model.HotelOwner;
import com.springboot.cozyheaven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelOwnerRepository extends JpaRepository<HotelOwner,Integer> {

    @Query("""
            select h from HotelOwner h
            join h.user u
            where u.username=?1
            """)
    HotelOwner getOwnerByUsername(String name);

}
