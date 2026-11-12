package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.CancellationDto;
import com.springboot.cozyheaven.enums.BookingStatus;
import com.springboot.cozyheaven.enums.PaymentStatus;
import com.springboot.cozyheaven.enums.RefundStatus;
import com.springboot.cozyheaven.enums.RoomStatus;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.CancellationMapper;
import com.springboot.cozyheaven.model.Bookings;
import com.springboot.cozyheaven.model.Cancellation;
import com.springboot.cozyheaven.model.Payment;
import com.springboot.cozyheaven.repository.BookingsRepository;
import com.springboot.cozyheaven.repository.CancellationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CancellationService {

    private final CancellationRepository cancellationRepository;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final BookingsRepository bookingsRepository;

    public void cancel(@Valid CancellationDto cancellationDto, int bid, String name) {

        Cancellation cancellation = CancellationMapper.getMappedforCancellation(cancellationDto);
        Bookings bookings = bookingService.getById(bid);

        if(!bookings.getCustomer().getUser().getUsername().equals(name))
            throw new OwnershipException("Payment can be cancelled by auth user only!");

        if (bookings.getStatus() == BookingStatus.CANCELLED)
            throw new ResourceNotFoundException("Booking is cancelled");


        if(paymentService.getByBookingId(bid) != null){

            Payment payment = paymentService.getByBookingId(bid);

            if (payment.getPaymentStatus() == PaymentStatus.PENDING){

                cancellation.setRefundStatus(RefundStatus.NA);
                cancellation.setCancelledDate(LocalDateTime.now());
                bookings.getRoom().setStatus(RoomStatus.AVAILABLE);
                payment.setPaymentStatus(PaymentStatus.NA);
            }

            if(payment.getPaymentStatus() == PaymentStatus.SUCCESS){

                cancellation.setRefundStatus(RefundStatus.PENDING);
                cancellation.setCancelledDate(LocalDateTime.now());
                cancellation.setRefundAmount(paymentService.getByBookingId(bid).getAmount());
                bookings.getRoom().setStatus(RoomStatus.AVAILABLE);
                payment.setPaymentStatus(PaymentStatus.REFUNDED);
            }

            bookings.setStatus(BookingStatus.CANCELLED);
            cancellation.setBookings(bookings);
            cancellationRepository.save(cancellation);

        }

        if(paymentService.getByBookingId(bid)== null && bookings.getStatus() == BookingStatus.CONFIRMED){

            cancellation.setRefundStatus(RefundStatus.NA);
            cancellation.setCancelledDate(LocalDateTime.now());

            bookings.getRoom().setStatus(RoomStatus.AVAILABLE);

            bookings.setStatus(BookingStatus.CANCELLED);
            bookingsRepository.save(bookings);
        }

    }

    public List<Cancellation> allOfCustomer(String name) {

        return cancellationRepository.allOFCustomer(name);

    }

}
