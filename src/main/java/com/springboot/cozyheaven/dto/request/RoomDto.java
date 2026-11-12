package com.springboot.cozyheaven.dto.request;

import com.springboot.cozyheaven.enums.AC;
import com.springboot.cozyheaven.enums.BedType;
import jakarta.validation.constraints.*;

public record RoomDto(
        @NotNull(message = "required")
        Integer hotelId,
        @NotNull(message = "RoomType required")
        @NotBlank(message = "RoomType required")
        String roomType,
        @NotNull(message = "BedType required")
        BedType bedType,
        @NotNull(message = "RoomSize required")
        @NotBlank(message = "RoomSize required")
        String roomSize,
        @Min(value = 1,message = "Minimum 1")
        @Max(value = 10,message = "Maximum 10")
        Integer maxGuest,
        @NotNull(message = "BasePrice required")
        Double basePrice,
        @NotNull(message = "Ac/Non-ac required")
        AC acType

) {
}
