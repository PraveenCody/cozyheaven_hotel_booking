package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.BookingsDto;
import com.springboot.cozyheaven.dto.response.BookingsStatusDto;
import com.springboot.cozyheaven.dto.response.ChartDtoBookings;
import com.springboot.cozyheaven.dto.response.RoomStatusDto;
import com.springboot.cozyheaven.enums.BedType;
import com.springboot.cozyheaven.enums.BookingStatus;
import com.springboot.cozyheaven.enums.RoomStatus;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.BookingsMapper;
import com.springboot.cozyheaven.model.*;
import com.springboot.cozyheaven.repository.BookingsRepository;
import com.springboot.cozyheaven.repository.CustomerRepository;
import com.springboot.cozyheaven.repository.HotelOwnerRepository;
import com.springboot.cozyheaven.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingsRepository bookingsRepository;
    private final RoomService roomService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final HotelOwnerRepository hotelOwnerRepository;

    public static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public void addBookings(BookingsDto bookingsDto, String name) {

        Bookings bookings = BookingsMapper.getMappedForBooking(bookingsDto);

        logger.info("Bookings in process");

        Customer customer = customerRepository.getCustomerByUsername(name);

        Room room = roomService.getRoomById(bookingsDto.roomId());

        if (customer == null)
            throw new ResourceNotFoundException("Customer not found");

        if (room == null)
            throw new ResourceNotFoundException("Room not found");

        if (bookingsDto.checkIn().isBefore(LocalDateTime.now())) {
            logger.info("Room not avail on this dates");
            throw new IllegalArgumentException("Check-in date must be in the future");
        }

        if (!bookingsDto.checkOut().isAfter(bookingsDto.checkIn())) {
            logger.info("Room not avail on this dates");
            throw new IllegalArgumentException("Check-out must be after check-in");
        }

        boolean alreadyBooked = bookingsRepository.isRoomAlreadyBooked(
                room.getId(),
                bookingsDto.checkIn(),
                bookingsDto.checkOut()
        );

        if (alreadyBooked) {
            logger.info("Room not avail on this dates");
            throw new ResourceNotFoundException("Room is already booked for the selected dates");
        }

        room.setStatus(RoomStatus.BOOKED);
        bookings.setRoom(room);
        bookings.setBookingDate(LocalDateTime.now());
        bookings.setStatus(BookingStatus.CONFIRMED);
        bookings.setCustomer(customer);

        bookings.setTotalAmount(
                calculateRoomFare(
                        room.getBedType(),
                        room.getMaxGuest(),
                        bookingsDto.noOfAdults(),
                        bookingsDto.noOfChildren(),
                        room.getBasePrice()
                )
        );

        bookingsRepository.save(bookings);
    }


    public double calculateRoomFare(
            BedType bedType,
            int maxGuest,
            int noOfAdults,
            int noOfChildren,
            double baseFare) {

        int totalGuests = noOfAdults + noOfChildren;

        if (totalGuests <= 0 || totalGuests > maxGuest) {
            throw new IllegalArgumentException("Invalid number of guests");
        }

        int includedGuests;

        if (maxGuest == 2) {
            includedGuests = 1;
        } else if (maxGuest == 4) {
            includedGuests = 2;
        } else if (maxGuest == 6) {
            includedGuests = 4;
        } else {
            throw new IllegalArgumentException("Invalid room configuration");
        }

        int includedAdults = Math.min(noOfAdults, includedGuests);

        int remainingSlots = includedGuests - includedAdults;

        int includedChildren = Math.min(noOfChildren, remainingSlots);

        int extraAdults = noOfAdults - includedAdults;
        int extraChildren = noOfChildren - includedChildren;

        double adultCharge = extraAdults * (baseFare * 0.40);
        double childCharge = extraChildren * (baseFare * 0.20);

        return baseFare + adultCharge + childCharge;
    }


    public List<Bookings> allBookings(String name) {

        User user = userRepository.getUserByUserName(name);

        return bookingsRepository.findAllByUsername(name);
    }

    public List<Bookings> allBookingsForAdmin(){
        return bookingsRepository.findAll();
    }

    public Bookings getById(int id) {

        Optional<Bookings> bookings = bookingsRepository.findById(id);

        if(bookings.isEmpty())
            throw new ResourceNotFoundException("Booking id not found");

        return bookings.get();
    }

    public List<Bookings> getBookingsByHotelOwner(String name) {

        HotelOwner owner = hotelOwnerRepository.getOwnerByUsername(name);

        if(owner==null)
            throw new ResourceNotFoundException("Hotel owner not found");

        return bookingsRepository.getBookingsByOwnerID(owner.getId());

    }



    public ChartDtoBookings chartAdmin() {
        List<BookingsStatusDto> s = bookingsRepository.adminDonut();
        return BookingsMapper.getMappedForChart(s);
    }

    public ChartDtoBookings chart(String name) {
        List<BookingsStatusDto> s = bookingsRepository.donut(name);
        return BookingsMapper.getMappedForChart(s);
    }
}
