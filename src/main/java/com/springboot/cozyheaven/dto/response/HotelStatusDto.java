package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.HotelStatus;

public record HotelStatusDto (
    HotelStatus hotelStatus,
    long noOfHotels
){
}
