package com.springboot.cozyheaven.model;

import com.springboot.cozyheaven.enums.AC;
import com.springboot.cozyheaven.enums.BedType;
import com.springboot.cozyheaven.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String roomType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BedType bedType;

    @Column(nullable = false)
    private String roomSize;

    @Column(nullable = false)
    private int maxGuest;

    @Column(nullable = false)
    private double basePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AC conditioning ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @ManyToOne
    @JoinColumn(name="hotel_id",nullable = false)
    private Hotel hotel;

}

