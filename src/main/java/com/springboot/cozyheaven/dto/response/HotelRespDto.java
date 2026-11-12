package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.HotelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HotelRespDto(

        int hid,

        int ownerId,

        @NotBlank(message = "Name is required")
        @NotNull(message = "Name is required")
        String name,

        @NotBlank(message = "Description is required")
        @NotNull(message = "Description is required")
        String description,

        @NotBlank(message = "location is required")
        @NotNull(message = "location is required")
        String location,

        @NotBlank(message = "city is required")
        @NotNull(message = "city is required")
        String city,

        @NotBlank(message = "imgurl required")
        @NotNull(message = "imgurl required")
        String imgUrl,

        @NotBlank(message = "required")
        Double overAllRatings,

        @NotNull(message = "required")
        HotelStatus status
) {
}
