
package com.springboot.cozyheaven;

import com.springboot.cozyheaven.dto.request.CancellationDto;
import com.springboot.cozyheaven.enums.*;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.CancellationMapper;
import com.springboot.cozyheaven.model.*;
import com.springboot.cozyheaven.repository.BookingsRepository;
import com.springboot.cozyheaven.repository.CancellationRepository;
import com.springboot.cozyheaven.service.BookingService;
import com.springboot.cozyheaven.service.CancellationService;
import com.springboot.cozyheaven.service.PaymentService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancellationTest {

    @Mock
    private CancellationRepository cancellationRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private BookingsRepository bookingsRepository;

    @InjectMocks
    private CancellationService cancellationService;

    private MockedStatic<CancellationMapper> cancellationMapper;

    private CancellationDto cancellationDto;
    private Cancellation cancellation;
    private Bookings bookings;
    private Customer customer;
    private User user;
    private Room room;
    private Payment payment;

    @BeforeEach
    public void setUp() {

        cancellationDto = mock(CancellationDto.class);
        cancellation = mock(Cancellation.class);

        bookings = mock(Bookings.class);
        customer = mock(Customer.class);
        user = mock(User.class);
        room = mock(Room.class);
        payment = mock(Payment.class);

        cancellationMapper = mockStatic(CancellationMapper.class);

        cancellationMapper.when(() ->
                CancellationMapper.getMappedforCancellation(cancellationDto)
        ).thenReturn(cancellation);

        when(bookings.getCustomer()).thenReturn(customer);
        when(customer.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("praveen");

        when(bookings.getRoom()).thenReturn(room);
    }

    @AfterEach
    public void tearDown() {
        cancellationMapper.close();
    }

    // 1. Cancel booking when payment is pending

    @Test
    public void cancelPendingPaymentTest() {

        when(bookingService.getById(1)).thenReturn(bookings);
        when(bookings.getStatus()).thenReturn(BookingStatus.CONFIRMED);
        when(paymentService.getByBookingId(1)).thenReturn(payment);
        when(payment.getPaymentStatus()).thenReturn(PaymentStatus.PENDING);

        cancellationService.cancel(cancellationDto, 1, "praveen");

        verify(cancellation).setRefundStatus(RefundStatus.NA);
        verify(cancellation).setCancelledDate(any());
        verify(room).setStatus(RoomStatus.AVAILABLE);
        verify(payment).setPaymentStatus(PaymentStatus.NA);
        verify(bookings).setStatus(BookingStatus.CANCELLED);
        verify(cancellation).setBookings(bookings);
        verify(cancellationRepository).save(cancellation);
    }

    // 2. Cancel booking when payment is successful

    @Test
    public void cancelSuccessfulPaymentTest() {

        when(bookingService.getById(1)).thenReturn(bookings);
        when(bookings.getStatus()).thenReturn(BookingStatus.CONFIRMED);
        when(paymentService.getByBookingId(1)).thenReturn(payment);
        when(payment.getPaymentStatus()).thenReturn(PaymentStatus.SUCCESS);

        cancellationService.cancel(cancellationDto, 1, "praveen");

        verify(cancellation).setRefundStatus(RefundStatus.PENDING);
        verify(cancellation).setCancelledDate(any());
        verify(cancellation).setRefundAmount(payment.getAmount());
        verify(room).setStatus(RoomStatus.AVAILABLE);
        verify(payment).setPaymentStatus(PaymentStatus.REFUNDED);
        verify(bookings).setStatus(BookingStatus.CANCELLED);
        verify(cancellation).setBookings(bookings);
        verify(cancellationRepository).save(cancellation);
    }


    // 5. Cancel booking when payment does not exist

    @Test
    public void cancelWithoutPaymentTest() {

        when(bookingService.getById(1)).thenReturn(bookings);
        when(bookings.getStatus()).thenReturn(BookingStatus.CONFIRMED);
        when(paymentService.getByBookingId(1)).thenReturn(null);

        cancellationService.cancel(cancellationDto, 1, "praveen");

        verify(cancellation).setRefundStatus(RefundStatus.NA);
        verify(cancellation).setCancelledDate(any());
        verify(room).setStatus(RoomStatus.AVAILABLE);
        verify(bookings).setStatus(BookingStatus.CANCELLED);
        verify(bookingsRepository).save(bookings);

        verify(cancellationRepository, never()).save(any());
    }



}
