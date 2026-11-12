package com.springboot.cozyheaven.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
public class HotelOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @Size(min = 10,max = 10)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Email
    @Column(nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

}
