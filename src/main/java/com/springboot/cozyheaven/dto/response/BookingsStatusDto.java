package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.BookingStatus;

public record BookingsStatusDto(
        BookingStatus bookingStatus,
        long noOfBookings
) {
}
