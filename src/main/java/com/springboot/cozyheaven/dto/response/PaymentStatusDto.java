package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.PaymentStatus;

public record PaymentStatusDto(
        PaymentStatus status,
        long noOfBookings
) {
}
