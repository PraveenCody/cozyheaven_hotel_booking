package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.PaymentDto;
import com.springboot.cozyheaven.dto.response.ChartDtoPayment;
import com.springboot.cozyheaven.dto.response.PaymentStatusDto;
import com.springboot.cozyheaven.enums.PaymentStatus;
import com.springboot.cozyheaven.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentMapper {

    public static Payment getMappedForPayment(PaymentDto paymentDto){
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentDto.paymentMethod());
        payment.setTransactionId(paymentDto.transactionId());
        return payment;
    }

    public static ChartDtoPayment getMappedForChart(List<PaymentStatusDto> list){

        List<PaymentStatus> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        list.forEach(e->{
            labels.add(e.status());
            values.add(e.noOfBookings());
        });

        return new ChartDtoPayment(
                labels,
                values
        );

    }

}
