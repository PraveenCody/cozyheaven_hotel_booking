package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.AC;
import com.springboot.cozyheaven.enums.BedType;

public record RoomRespDto(
        int roomId,
        String hotelName,
        String description,
        String location,
        String city,
        String roomType,
        BedType bedType,
        String roomSize,
        double basePrice,
        AC conditioning,
        double overAllRating,
        String imgurl
) {
}
