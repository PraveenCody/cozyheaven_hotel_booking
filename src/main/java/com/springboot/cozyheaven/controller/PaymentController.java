package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.PaymentDto;
import com.springboot.cozyheaven.dto.response.ChartDtoPayment;
import com.springboot.cozyheaven.model.Payment;
import com.springboot.cozyheaven.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment/")
    public void doPayment(@RequestParam(name = "bid") int bid, @RequestBody PaymentDto paymentDto, Principal principal){
        paymentService.doPayment(paymentDto,bid,principal.getName());
    }

    @GetMapping("/payment/myPayments/")
    public List<Payment> myPayments(Principal principal){
        return paymentService.myPayments(principal.getName());
    }

    @GetMapping("/payment/getAll/")
    public List<Payment> getAll(){
        return paymentService.getAllPayments();
    }

    @GetMapping("/payment/getById/{bid}")
    public Payment getById(@PathVariable int bid){
        return paymentService.getByBookingId(bid);
    }

    @GetMapping("/payment/chart/")
    public ChartDtoPayment getChart(Principal principal){
        return paymentService.getChart(principal.getName());
    }

    @GetMapping("/payment/chart/admin/")
    public ChartDtoPayment getChart(){
        return paymentService.getChartAdmin();
    }

}
