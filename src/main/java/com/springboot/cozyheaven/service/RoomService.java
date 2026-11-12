package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.RoomDto;
import com.springboot.cozyheaven.dto.response.*;
import com.springboot.cozyheaven.enums.AC;
import com.springboot.cozyheaven.enums.BedType;
import com.springboot.cozyheaven.enums.RoomStatus;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.HotelMapper;
import com.springboot.cozyheaven.mapper.RoomMapper;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.model.Room;
import com.springboot.cozyheaven.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelService hotelService;

    public void addRoom(RoomDto roomDto,String name){

        Hotel hotel = hotelService.getById(roomDto.hotelId(),name);

        if(hotel==null)
            throw new ResourceNotFoundException("Hotel not found");

        if(!hotel.getHotelOwner().getUser().getUsername().equals(name))
            throw new OwnershipException("Your not authorized to create Room");

        Room room = RoomMapper.getMappeddForRoom(roomDto);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setHotel(hotel);
        roomRepository.save(room);

    }

    public Room getById(int id,String name){
        Optional<Room> room = roomRepository.findById(id);

        if(room.isEmpty())
            throw new ResourceNotFoundException("Id not found");

        if(!room.get().getHotel().getHotelOwner().getUser().getUsername().equals(name))
            throw new OwnershipException("Cant get, need auth");

        return room.get();
    }

    public Room getRoomById(int id) {

        Optional<Room> room = roomRepository.findById(id);

        if(room.isEmpty())
            throw new ResourceNotFoundException("Id not found");

        return room.get();
    }

    public void update(int id, String roomType, BedType bedType, String roomSize, Integer maxGuest, double basePrice, AC acType, RoomStatus status,String name) {

        Room room = getById(id,name);

        if(roomType !=null && !roomType.isEmpty())
            room.setRoomType(roomType);

        if(roomSize !=null && !roomSize.isEmpty())
            room.setRoomSize(roomSize);

        if(bedType !=null )
            room.setBedType(bedType);

        if(basePrice != 0)
            room.setBasePrice(basePrice);

        if (maxGuest != null)
            if (maxGuest != 0) {
                room.setMaxGuest(maxGuest);
            }

        if(acType != null)
            room.setConditioning(acType);

        if(status!=null)
            room.setStatus(status);

        roomRepository.save(room);

    }

    public void delete(int id){
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }

    public List<Room> getAllByOwner(String name) {
        return roomRepository.getAllByOwner(name);
    }

    public List<RoomRespDto> dynamicSearch(String city,LocalDateTime checkIn, LocalDateTime checkOut, Integer noOfAdults, Integer noOfChildren) {
        return roomRepository.dynamicSearch(city,checkIn,checkOut,noOfAdults,noOfChildren);
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public List<Room> getAllByHotel(String name,int id) {
        return roomRepository.getAllByHotel(id);
    }

    public ChartDtoRoom chartAdmin() {
        List<RoomStatusDto> list = roomRepository.chartAdmin();
        return RoomMapper.getMappedForChart(list);
    }
}
