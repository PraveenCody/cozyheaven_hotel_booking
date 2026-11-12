package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.HotelDto;
import com.springboot.cozyheaven.dto.response.ChartDtoBookings;
import com.springboot.cozyheaven.dto.response.ChartDtoHotel;
import com.springboot.cozyheaven.dto.response.ChartDtoRoom;
import com.springboot.cozyheaven.dto.response.HotelRespDto;
import com.springboot.cozyheaven.enums.HotelStatus;
import com.springboot.cozyheaven.mapper.HotelMapper;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class HotelController {

    private final HotelService hotelService;

    @PostMapping(
            value = "/hotel/add/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void add(@Valid @RequestPart("hotel") HotelDto hotelDto, @RequestPart("image") MultipartFile image, Principal principal) throws IOException {
        hotelService.add(hotelDto,image,principal.getName());
    }

    @GetMapping("/hotel/get/{id}")
    public Hotel getHotelById(@PathVariable int id,Principal principal){
         return hotelService.getById(id,principal.getName());
    }

    @GetMapping("/cus/hotel/{id}")
    public HotelRespDto getHotel(@PathVariable int id, Principal principal){
        return HotelMapper.getMappedForHotelResponse(hotelService.getHotelById(id));
    }

    @GetMapping("/hotel/getAllByHotelOwner/")
    public List<Hotel> getAllByUsername(Principal principal){
        return hotelService.getAllByOwner(principal.getName());
    }

    @GetMapping("/hotel/getAll/")
    public List<HotelRespDto> getAllHotel(){
        List<Hotel> hotels = hotelService.getAll();
        List<HotelRespDto> hotelRespDtos = new ArrayList<>();

        for(Hotel hotel: hotels){
            hotelRespDtos.add(HotelMapper.getMappedForHotelResponse(hotel));
        }

        return hotelRespDtos;
    }

    @PutMapping(
            value = "/hotel/update/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void update(
            @RequestParam(name = "id",required = true)int id,
            @RequestParam(name = "name",required = false)String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "location", required = false)String location,
            @RequestParam(name="city", required = false)String city,
            @RequestParam(name="status",required=false) HotelStatus status,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Principal principal) throws IOException {
        hotelService.update(id,name,description,location,city,status,img,principal.getName());
    }

    @DeleteMapping("/hotel/delete/{id}")
    public void delete(@PathVariable int id){
        hotelService.delete(id);
    }

    @GetMapping("/hotel/chart/admin/")
    public ChartDtoHotel chart(){
        return hotelService.chartAdmin();
    }

}
