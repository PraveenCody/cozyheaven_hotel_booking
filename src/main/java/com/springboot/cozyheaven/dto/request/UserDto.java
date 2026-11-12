package com.springboot.cozyheaven.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(
        @NotNull(message = "required")
        @NotBlank(message = "required")
        String name,
        @NotNull(message = "required")
        @NotBlank(message = "required")
        String password
) {
}
