package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.HotelStatus;
import com.springboot.cozyheaven.enums.PaymentStatus;

import java.util.List;

public record ChartDtoHotel(
        List<HotelStatus> labels,
        List<Long> values
) {
}
