package com.springboot.cozyheaven.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AmenityDto(

        @NotNull(message = "Name Required")
        @NotBlank(message = "Name Required")
        String name,

        @NotNull(message = "Icon Required")
        @NotBlank(message = "Icon Required")
        String icon

) {
}