package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.BookingStatus;

import java.util.List;

public record ChartDtoBookings(
        List<BookingStatus> labels,
        List<Long> values
) {
}