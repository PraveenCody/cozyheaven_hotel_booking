package com.springboot.cozyheaven.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HotelDto(
        @NotBlank(message = "Name is required")
        @NotNull(message = "Name is required")
        String name,

        @NotBlank(message = "Description is required")
        @NotNull(message = "Description is required")
        String description,

        @NotBlank(message = "location is required")
        @NotNull(message = "location is required")
        String location,

        @NotBlank(message = "city is required")
        @NotNull(message = "city is required")
        String city
) {
}
