package com.springboot.cozyheaven.dto.response;

import com.springboot.cozyheaven.enums.RoomStatus;

public record RoomStatusDto(
        RoomStatus roomStatus,
        long noOfRoom
) {
}
