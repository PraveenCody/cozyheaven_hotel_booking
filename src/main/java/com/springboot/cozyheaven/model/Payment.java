package com.springboot.cozyheaven.model;

import com.springboot.cozyheaven.enums.PaymentMethod;
import com.springboot.cozyheaven.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class Payment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Bookings booking;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupons coupon;

    private double discountAmount;

}
