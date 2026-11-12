package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.HotelOwnerDto;
import com.springboot.cozyheaven.model.HotelOwner;

public class HotelOwnerMapper {

    public static HotelOwner getMappedToHotelOWner(HotelOwnerDto hotelOwnerDto){

        HotelOwner hotelOwner = new HotelOwner();
        hotelOwner.setName(hotelOwnerDto.name());
        hotelOwner.setAddress(hotelOwnerDto.address());
        hotelOwner.setPhone(hotelOwnerDto.phone());
        hotelOwner.setEmail(hotelOwnerDto.email());
        return hotelOwner;

    }

}
