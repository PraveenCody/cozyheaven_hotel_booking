package com.springboot.cozyheaven;

import com.springboot.cozyheaven.dto.request.PaymentDto;
import com.springboot.cozyheaven.enums.*;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.PaymentMapper;
import com.springboot.cozyheaven.model.*;
import com.springboot.cozyheaven.repository.PaymentRepository;
import com.springboot.cozyheaven.service.BookingService;
import com.springboot.cozyheaven.service.CouponService;
import com.springboot.cozyheaven.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private CouponService couponService;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    public void doPaymentFixedCouponTest() {

        User user = new User();
        user.setUsername("jesse");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalAmount(5000.0);

        Coupons coupon = new Coupons();
        coupon.setStatus(CouponStatus.ACTIVE);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(2));
        coupon.setDiscountType(DiscountType.FIXED);
        coupon.setCode("SAVE500");
        coupon.setDiscountValue(500.0);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(2));

        PaymentDto paymentDto =
                new PaymentDto(PaymentMethod.UPI, "SFDBGF548", "SAVE500");

        Payment payment = new Payment();

        when(bookingService.getById(1)).thenReturn(booking);
        when(couponService.getCouponByCode("SAVE500")).thenReturn(coupon);

        MockedStatic<PaymentMapper> mapper = mockStatic(PaymentMapper.class);

        mapper.when(() -> PaymentMapper.getMappedForPayment(paymentDto))
                .thenReturn(payment);

        paymentService.doPayment(paymentDto, 1, "jesse");

        assertEquals(4500.0, payment.getAmount());
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());

        verify(paymentRepository, times(1)).save(payment);

        mapper.close();
    }

    @Test
    public void doPaymentPercentCouponTest() {

        User user = new User();
        user.setUsername("jesse");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalAmount(5000.0);

        PaymentDto paymentDto = mock(PaymentDto.class);
        when(paymentDto.couponCode()).thenReturn("SAVE10");

        Coupons coupon = new Coupons();
        coupon.setStatus(CouponStatus.ACTIVE);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(2));
        coupon.setDiscountType(DiscountType.PERCENT);
        coupon.setDiscountValue(10.0);

        Payment payment = new Payment();

        when(bookingService.getById(1)).thenReturn(booking);
        when(couponService.getCouponByCode("SAVE10")).thenReturn(coupon);

        MockedStatic<PaymentMapper> mapper =
                     mockStatic(PaymentMapper.class);

        mapper.when(() -> PaymentMapper.getMappedForPayment(paymentDto))
                .thenReturn(payment);

        paymentService.doPayment(paymentDto, 1, "jesse");

        assertEquals(500.0, payment.getDiscountAmount());
        assertEquals(4500.0, payment.getAmount());
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());

        verify(paymentRepository, times(1)).save(payment);

    }

    @Test
    public void doPaymentOwnershipExceptionTest() {

        User user = new User();
        user.setUsername("gus fring");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);

        when(bookingService.getById(1)).thenReturn(booking);

        PaymentDto paymentDto = mock(PaymentDto.class);

        assertThrows(OwnershipException.class,
                () -> paymentService.doPayment(paymentDto, 1, "jesse"));

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void doPaymentCancelledBookingTest() {

        User user = new User();
        user.setUsername("jesse");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.CANCELLED);

        when(bookingService.getById(1)).thenReturn(booking);

        PaymentDto paymentDto = mock(PaymentDto.class);

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.doPayment(paymentDto, 1, "jesse"));

        verify(paymentRepository, never()).save(any(Payment.class));
    }


    @Test
    public void getByBookingIdTest() {

        Payment payment = new Payment();

        when(paymentRepository.getByBookingId(1)).thenReturn(payment);

        Payment result = paymentService.getByBookingId(1);

        assertNotNull(result);
        assertEquals(payment, result);

        verify(paymentRepository, times(1)).getByBookingId(1);
    }

    @Test
    public void getAllPaymentsTest() {

        Payment payment1 = new Payment();
        Payment payment2 = new Payment();

        List<Payment> payments = List.of(payment1, payment2);

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        assertEquals(payments, result);

        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    public void getAllPaymentsEmptyTest() {

        when(paymentRepository.findAll()).thenReturn(List.of());

        List<Payment> result = paymentService.getAllPayments();

        assertTrue(result.isEmpty());

        verify(paymentRepository, times(1)).findAll();
    }

}
