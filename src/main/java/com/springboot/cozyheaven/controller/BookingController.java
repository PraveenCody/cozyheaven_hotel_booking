package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.BookingsDto;
import com.springboot.cozyheaven.dto.response.BookingRespDto;
import com.springboot.cozyheaven.dto.response.ChartDtoBookings;
import com.springboot.cozyheaven.model.Bookings;
import com.springboot.cozyheaven.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/booking/add/")
    public void add(@Valid @RequestBody BookingsDto bookingsDto,Principal principal){
        bookingService.addBookings(bookingsDto,principal.getName());
    }

    @GetMapping("/booking/getById/{id}")
    public BookingRespDto getById(@PathVariable int id){
        Bookings b = bookingService.getById(id);
        return new BookingRespDto(b.getId(),b.getCheckIn(),b.getCheckOut(),b.getNoOfAdults(),b.getNoOfChildren(),b.getTotalAmount(),b.getStatus(),b.getBookingDate(),b.getRoom().getId());
    }

    @GetMapping("/bookings/MyBookings/")
    public List<BookingRespDto> getAllBookingsOfCustomer(Principal principal){
        List<Bookings> bookings =  bookingService.allBookings(principal.getName());
        List<BookingRespDto> resp = new ArrayList<>();

        for(Bookings b:bookings){
           resp.add(new BookingRespDto(b.getId(),b.getCheckIn(),b.getCheckOut(),b.getNoOfAdults(),b.getNoOfChildren(),b.getTotalAmount(),b.getStatus(),b.getBookingDate(),b.getRoom().getId()));
        }
        return resp;
    }

    @GetMapping("/booking/getByOwner/")
    public List<Bookings> getBookingsByHotelOwner(Principal principal){
        return bookingService.getBookingsByHotelOwner(principal.getName());
    }

    @GetMapping("/bookings/all/")
    public List<Bookings> getAllBookings(){
        return bookingService.allBookingsForAdmin();
    }

    @GetMapping("/bookings/chart/")
    public ChartDtoBookings chart(Principal principal){
        return bookingService.chart(principal.getName());
    }

    @GetMapping("/bookings/chart/admin/")
    public ChartDtoBookings chart(){
        return bookingService.chartAdmin();
    }

}
