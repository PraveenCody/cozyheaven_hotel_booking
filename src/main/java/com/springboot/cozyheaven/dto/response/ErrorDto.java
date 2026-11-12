package com.springboot.cozyheaven.dto.response;

import java.time.Instant;

public record ErrorDto(
        String error,
        String description,
        Instant time
) {
}
