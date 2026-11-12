package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.HotelOwnerDto;
import com.springboot.cozyheaven.dto.response.HotelOwnerRespDto;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.model.HotelOwner;
import com.springboot.cozyheaven.service.HotelOwnerService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class HotelOwnerController {

    private final HotelOwnerService hotelOwnerService;

    @PostMapping("/hotelOwner/add/")
    public void insert(@Valid @RequestBody HotelOwnerDto hotelOwnerDto){
        hotelOwnerService.insert(hotelOwnerDto);
    }

    @GetMapping("/hotelOwner/")
    public HotelOwner getOwner(Principal principal){
        return hotelOwnerService.getOwner(principal.getName());
    }

    @GetMapping("/hotelOwner/getHotelOwner/{id}")
    public HotelOwner getById(@PathVariable int id){
        return hotelOwnerService.getOwnerById(id);
    }

    @GetMapping("/hotelOwner/getAll/")
    public List<HotelOwner> getAll(){
        return hotelOwnerService.getAll();
    }

    @PutMapping("/hotelOwner/update/")
    public void update( @RequestParam(name = "id", required = true) int id,
                        @RequestParam(name = "name",required = false) String name,
                        @RequestParam(name = "phone",required = false) String phone,
                        @RequestParam(name = "address", required = false)String address,
                        Principal principal){
        hotelOwnerService.update(id,name,phone,address,principal.getName());
    }

    @DeleteMapping("/hotelOwner/delete/{id}")
    public void delete(@PathVariable int id){
        hotelOwnerService.delete(id);
    }

}