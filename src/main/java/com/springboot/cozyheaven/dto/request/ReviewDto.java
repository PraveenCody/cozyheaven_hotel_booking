package com.springboot.cozyheaven.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReviewDto(

        @NotNull(message = "required")
        double rating,
        @NotNull(message = "required")
        @NotBlank(message = "required")
        String comments
) {
}
