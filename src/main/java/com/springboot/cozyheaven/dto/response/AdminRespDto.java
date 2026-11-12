package com.springboot.cozyheaven.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminRespDto(
        @NotNull(message = "Name is required")
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Number is required")
        @NotBlank(message = "Number is required")
        String phone,

        @Email
        String email,

        @NotNull(message = "Name is required")
        @NotBlank(message = "Name is required")
        String username

) {
}
