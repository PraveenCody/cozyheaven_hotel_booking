package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.RoomDto;
import com.springboot.cozyheaven.dto.response.ChartDtoHotel;
import com.springboot.cozyheaven.dto.response.ChartDtoRoom;
import com.springboot.cozyheaven.dto.response.HotelStatusDto;
import com.springboot.cozyheaven.dto.response.RoomStatusDto;
import com.springboot.cozyheaven.enums.HotelStatus;
import com.springboot.cozyheaven.enums.RoomStatus;
import com.springboot.cozyheaven.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomMapper {

    public static Room getMappeddForRoom(RoomDto roomDto){

        Room room = new Room();
        room.setRoomType(roomDto.roomType());
        room.setRoomSize(roomDto.roomSize());
        room.setBedType(roomDto.bedType());
        room.setBasePrice(roomDto.basePrice());
        room.setMaxGuest(roomDto.maxGuest());
        room.setConditioning(roomDto.acType());
        return room;

    }

    public static ChartDtoRoom getMappedForChart(List<RoomStatusDto> list) {

        List<RoomStatus> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (RoomStatusDto e:list){
            labels.add(e.roomStatus());
            values.add(e.noOfRoom());
        }

        return new ChartDtoRoom(
                labels,
                values
        );

    }
}
