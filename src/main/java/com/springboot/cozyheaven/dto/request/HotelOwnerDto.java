package com.springboot.cozyheaven.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HotelOwnerDto(
        @NotNull(message = "Name Required")
        @NotBlank(message = "Name Required")
        String name,

        @NotNull(message = "Phone Required")
        @NotBlank(message = "Phone Required")
        @Size(min = 10,max = 10)
        String phone,

        @NotNull(message = "Address Required")
        @NotBlank(message = "Address Required")
        String address,

        @Email
        String email,

        @NotNull(message = "Username Required")
        @NotBlank(message = "Username Required")
        String username,

        @NotNull(message = "Password Required")
        @NotBlank(message = "Password Required")
        String password
) {
}
