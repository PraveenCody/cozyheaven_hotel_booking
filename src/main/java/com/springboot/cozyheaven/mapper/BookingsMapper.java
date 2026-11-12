package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.BookingsDto;
import com.springboot.cozyheaven.dto.response.BookingsStatusDto;
import com.springboot.cozyheaven.dto.response.ChartDtoBookings;
import com.springboot.cozyheaven.enums.BookingStatus;
import com.springboot.cozyheaven.enums.RoomStatus;
import com.springboot.cozyheaven.model.Bookings;

import java.util.ArrayList;
import java.util.List;

public class BookingsMapper {

    public static Bookings getMappedForBooking(BookingsDto bookingsDto){

        Bookings bookings = new Bookings();
        bookings.setCheckIn(bookingsDto.checkIn());
        bookings.setCheckOut(bookingsDto.checkOut());
        bookings.setNoOfAdults(bookingsDto.noOfAdults());
        bookings.setNoOfChildren(bookingsDto.noOfChildren());
        return bookings;

    }

    public static ChartDtoBookings getMappedForChart(List<BookingsStatusDto> list){

        List<BookingStatus> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (BookingsStatusDto e:list){
            labels.add(e.bookingStatus());
            values.add(e.noOfBookings());
        }

        return new ChartDtoBookings(
                labels,
                values
        );

    }


}
