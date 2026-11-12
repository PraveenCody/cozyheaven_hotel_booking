package com.springboot.cozyheaven.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CancellationDto (
        @NotNull(message = "required")
        @NotBlank(message = "required")
        String reason
){
}
