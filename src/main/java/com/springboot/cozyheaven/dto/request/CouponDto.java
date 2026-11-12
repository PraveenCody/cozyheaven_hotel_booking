package com.springboot.cozyheaven.dto.request;

import com.springboot.cozyheaven.enums.CouponStatus;
import com.springboot.cozyheaven.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CouponDto(
        @NotBlank(message = "Required")
        @NotNull(message = "Required")
        String code,
        @NotNull(message = "Required")
        DiscountType discountType,
        @NotNull(message = "Required")
        Double discountValue,
        @NotNull(message = "Required")
        LocalDateTime expiryDate,
        @NotNull(message = "Required")
        CouponStatus status
) {
}
