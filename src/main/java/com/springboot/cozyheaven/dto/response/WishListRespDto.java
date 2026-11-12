package com.springboot.cozyheaven.dto.response;

import java.time.Instant;

public record WishListRespDto(
        String hotelName,
        String description,
        String location,
        String city,
        String imgUrl,
        double overAllRating,
        Instant addedDate

) {
}
