package com.springboot.cozyheaven.repository;

import com.fasterxml.classmate.members.RawMember;
import com.springboot.cozyheaven.dto.response.AdminRespDto;
import com.springboot.cozyheaven.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

    @Query("""
            select a from Admin a
            join a.user u
            where u.username=?1
            """)
    Admin getAdminByUsername(String principalName);

    @Query("""
            select a.name,a.phone,a.email,u.username from Admin a
            join a.user u
            """)
    List<AdminRespDto> findAllDto();
}
