package com.springboot.cozyheaven.dto.request;

import com.springboot.cozyheaven.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PaymentDto(
        @NotNull(message = "Required")
        PaymentMethod paymentMethod,
        @NotNull(message = "Required")
        @NotBlank(message = "Required")
        String transactionId,
        String couponCode
) {
}