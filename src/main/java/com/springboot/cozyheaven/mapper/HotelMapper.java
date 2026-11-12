package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.HotelDto;
import com.springboot.cozyheaven.dto.response.*;
import com.springboot.cozyheaven.enums.BookingStatus;
import com.springboot.cozyheaven.enums.HotelStatus;
import com.springboot.cozyheaven.model.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelMapper {

    public static Hotel getMappedForHotel(HotelDto hotelDto){

        Hotel hotel = new Hotel();
        hotel.setHotelName(hotelDto.name());
        hotel.setDescription(hotelDto.description());
        hotel.setLocation(hotelDto.location());
        hotel.setCity(hotelDto.city());
        return hotel;
    }

    public static HotelRespDto getMappedForHotelResponse(Hotel hotel){

        return new HotelRespDto(hotel.getHotelId(),hotel.getHotelOwner().getId(),
                hotel.getHotelName(),hotel.getDescription(),hotel.getLocation(),
                hotel.getCity(),hotel.getImgUrl(),hotel.getOverAllRating(),hotel.getStatus());
    }

    public static ChartDtoHotel getMappedForChart(List<HotelStatusDto> list){

        List<HotelStatus> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (HotelStatusDto e:list){
            labels.add(e.hotelStatus());
            values.add(e.noOfHotels());
        }

        return new ChartDtoHotel(
                labels,
                values
        );

    }


}
