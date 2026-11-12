package com.springboot.cozyheaven.model;

import com.springboot.cozyheaven.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter@Getter
@Entity
public class Cancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private double refundAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus refundStatus;

    @Column(nullable = false)
    private LocalDateTime cancelledDate;

    @OneToOne
    @JoinColumn(nullable = false)
    private Bookings bookings;

}
