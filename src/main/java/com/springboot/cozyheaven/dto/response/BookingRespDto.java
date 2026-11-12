package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.BookingStatus;
import com.springboot.cozyheaven.model.Room;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public record BookingRespDto(
        int bid,
        LocalDateTime checkIn,
        LocalDateTime checkOut,
        int noOfAdults,
        int noOfChildren,
        double totalAmt,
        BookingStatus status,
        LocalDateTime bookingDate,
        int roomId
) {
}
