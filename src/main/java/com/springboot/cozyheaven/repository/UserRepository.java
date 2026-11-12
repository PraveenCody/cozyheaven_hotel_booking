package com.springboot.cozyheaven.repository;

import com.springboot.cozyheaven.dto.response.DetailsDto;
import com.springboot.cozyheaven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("""
            select u from User u
            where u.username=?1
            """)
    User getUserByUserName(String name);

    @Query("""
            select u.username,u.isActive,u.role from User u
            where u.username=?1
            """)
    DetailsDto getUserForLogin(String name);

    @Query("""
            
            select u from User u
            where u.username = ?1 and u.isActive = com.springboot.cozyheaven.enums.Status.Active
            
            """)
    UserDetails loadUserByUsername(String username);

}
