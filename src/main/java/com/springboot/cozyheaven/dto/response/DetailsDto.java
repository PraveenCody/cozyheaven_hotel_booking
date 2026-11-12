package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.Role;
import com.springboot.cozyheaven.enums.Status;

public record DetailsDto(
        String username,
        Status isActive,
        Role role
) {
}
