package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.PaymentDto;
import com.springboot.cozyheaven.dto.response.ChartDtoPayment;
import com.springboot.cozyheaven.dto.response.PaymentStatusDto;
import com.springboot.cozyheaven.enums.BookingStatus;
import com.springboot.cozyheaven.enums.CouponStatus;
import com.springboot.cozyheaven.enums.DiscountType;
import com.springboot.cozyheaven.enums.PaymentStatus;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.PaymentMapper;
import com.springboot.cozyheaven.model.Bookings;
import com.springboot.cozyheaven.model.Coupons;
import com.springboot.cozyheaven.model.Payment;
import com.springboot.cozyheaven.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;
    private final CouponService couponService;

    public void doPayment(PaymentDto paymentDto, int bid,String name) {

        Bookings bookings = bookingService.getById(bid);

        if(!bookings.getCustomer().getUser().getUsername().equals(name))
            throw new OwnershipException("Payment can be proceeded by auth user only!");

        if (bookings.getStatus() == BookingStatus.CANCELLED)
            throw new ResourceNotFoundException("Booking is cancelled, cannot pay");

        Payment payment = PaymentMapper.getMappedForPayment(paymentDto);

        if(paymentDto.couponCode()!=null && !paymentDto.couponCode().isEmpty()){
            Coupons coupons = couponService.getCouponByCode(paymentDto.couponCode());

            if(coupons.getStatus() != CouponStatus.ACTIVE || coupons.getExpiryDate().isBefore(LocalDateTime.now()))
                throw new ResourceNotFoundException("CouponCode expired/IN-ACTIVE");

            if(coupons.getDiscountType() == DiscountType.FIXED)
                payment.setAmount(bookings.getTotalAmount()-coupons.getDiscountValue());

            if(coupons.getDiscountType() == DiscountType.PERCENT){
                payment.setDiscountAmount(bookings.getTotalAmount()*coupons.getDiscountValue()/100);
                payment.setAmount(bookings.getTotalAmount()-payment.getDiscountAmount());
            }
            payment.setCoupon(coupons);
        }
        else
            payment.setAmount(bookings.getTotalAmount());

        payment.setBooking(bookings);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        bookings.setStatus(BookingStatus.COMPLETED);
        paymentRepository.save(payment);
    }

    public Payment getByBookingId(int id){
        return paymentRepository.getByBookingId(id);
    }

    public Payment getById(int id){

        Optional<Payment> payment = paymentRepository.findById(id);

        if(payment.isEmpty())
            throw new ResourceNotFoundException("Id not found");

        return payment.get();
    }

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public ChartDtoPayment getChart(String name){
        List<PaymentStatusDto> list = paymentRepository.chart(name);
        return PaymentMapper.getMappedForChart(list);
    }

    public ChartDtoPayment getChartAdmin() {
        List<PaymentStatusDto> list = paymentRepository.chartAdmin();
        return PaymentMapper.getMappedForChart(list);
    }

    public List<Payment> myPayments(String name) {
        return paymentRepository.myPayments(name);
    }
}
