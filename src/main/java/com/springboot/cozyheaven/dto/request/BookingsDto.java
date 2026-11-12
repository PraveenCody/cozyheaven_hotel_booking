package com.springboot.cozyheaven.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingsDto(
        @NotNull(message = "required")
        int roomId,
        @NotNull(message = "required")
        LocalDateTime checkIn,
        @NotNull(message = "required")
        LocalDateTime checkOut,
        @Min(value = 1)
        int noOfAdults,
        int noOfChildren
) {
}
