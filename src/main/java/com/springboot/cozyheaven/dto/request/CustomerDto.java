package com.springboot.cozyheaven.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record CustomerDto(
        @NotNull(message = "Name is required")
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "Number is required")
        @NotBlank(message = "Number is required")
        String phone,
        @NotNull(message = "username is required")
        @NotBlank(message = "username is required")
        String username,
        @NotNull(message = "password is required")
        @NotBlank(message = "password is required")
        String password,
        @Email
        String email
) {
}
