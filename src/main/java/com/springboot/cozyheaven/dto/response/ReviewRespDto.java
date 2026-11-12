package com.springboot.cozyheaven.dto.response;

import java.time.LocalDateTime;

public record ReviewRespDto(
        int id,
        int pid,
        String hotelName,
        double rating,
        String comments,
        LocalDateTime reviewDate,
        LocalDateTime reviewUpdateDate
){
}
