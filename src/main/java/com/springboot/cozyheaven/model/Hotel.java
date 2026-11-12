package com.springboot.cozyheaven.model;

import com.springboot.cozyheaven.enums.HotelStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hotelId;

    @Column(nullable = false,unique = true)
    private String hotelName;

    @Column(nullable = false,length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String city;

    @Column(length = 1000)
    private String imgUrl;

    @Column(nullable = false)
    private double overAllRating=0.0;

    @Enumerated(EnumType.STRING)
    private HotelStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private HotelOwner hotelOwner;

}
