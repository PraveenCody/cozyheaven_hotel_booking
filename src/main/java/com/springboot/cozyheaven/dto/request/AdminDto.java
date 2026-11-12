package com.springboot.cozyheaven.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminDto(

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
        String username,

        @NotNull(message = "Name is required")
        @NotBlank(message = "Name is required")
        String password
) {
}
