package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.RoomDto;
import com.springboot.cozyheaven.dto.response.ChartDtoHotel;
import com.springboot.cozyheaven.dto.response.ChartDtoRoom;
import com.springboot.cozyheaven.dto.response.RoomRespDto;
import com.springboot.cozyheaven.enums.AC;
import com.springboot.cozyheaven.enums.BedType;
import com.springboot.cozyheaven.enums.RoomStatus;
import com.springboot.cozyheaven.model.Room;
import com.springboot.cozyheaven.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room/add/")
    public void add(@Valid @RequestBody RoomDto roomDto,Principal principal){
        roomService.addRoom(roomDto,principal.getName());
    }

    @PutMapping("/room/update/")
    public void update(@RequestParam(name="id")int id,
                       @RequestParam(name="roomType",required = false) String roomType,
                       @RequestParam(name="bedType",required = false)BedType bedType,
                       @RequestParam(name="roomSize",required = false)String roomSize,
                       @RequestParam(name = "maxGuest",required = false)Integer maxGuest,
                       @RequestParam(name="basePrice",required = false)Double basePrice,
                       @RequestParam(name="acType",required = false)AC acType,
                       @RequestParam(name = "status",required = false) RoomStatus status,
                       Principal principal){
        roomService.update(id,roomType, bedType,roomSize,maxGuest,basePrice,acType,status,principal.getName());
    }

    @GetMapping("/room/getRoom/{id}")
    public Room getById(@PathVariable int id,Principal principal){
        return roomService.getById(id,principal.getName());
    }

    @GetMapping("/room/seeRoom/{id}")
    public RoomRespDto getByIdForCustomer(@PathVariable int id){
        Room room = roomService.getRoomById(id);
        return new RoomRespDto(room.getId(),room.getHotel().getHotelName(),room.getHotel().getDescription(),
                room.getHotel().getLocation(),room.getHotel().getCity(),room.getRoomType(),room.getBedType(),room.getRoomSize(),room.getBasePrice(),
                room.getConditioning(),room.getHotel().getOverAllRating(),room.getHotel().getImgUrl());

    }

    @DeleteMapping("/room/delete/{id}")
    public void delete(@PathVariable int id){
        roomService.delete(id);
    }

    @GetMapping("/room/getAllByOwner/")
    public List<Room> getAllByOwner(Principal principal){
        return roomService.getAllByOwner(principal.getName());
    }

    @GetMapping("/room/getAllByHotel/{id}")
    public List<Room> getAllByHotel(Principal principal,@PathVariable int id){
        return roomService.getAllByHotel(principal.getName(),id);
    }

    @GetMapping("/room/dynamicSearch/")
    public List<RoomRespDto> dynamicSearch(@RequestParam(name = "city",required = false) String city,
                                           @RequestParam(name = "checkIn",required = false)LocalDateTime checkIn,
                                           @RequestParam(name = "checkOut",required = false)LocalDateTime checkOut,
                                           @RequestParam(name = "noOfAdults")Integer noOfAdults,
                                           @RequestParam(name = "noOfChildren",required = false,defaultValue = "0")Integer noOfChildren)
    {
        return roomService.dynamicSearch(city,checkIn,checkOut,noOfAdults,noOfChildren);
    }

    @GetMapping("/room/getAllRooms/")
    public List<Room> getAllRooms(){
        return roomService.getAll();
    }

    @GetMapping("/room/chart/admin/")
    public ChartDtoRoom chart(){
        return roomService.chartAdmin();
    }
}
