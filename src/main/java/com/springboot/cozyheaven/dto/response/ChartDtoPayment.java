package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.PaymentStatus;
import com.springboot.cozyheaven.enums.RoomStatus;

import java.util.List;

public record ChartDtoPayment(
        List<PaymentStatus> labels,
        List<Long> values
) {
}
