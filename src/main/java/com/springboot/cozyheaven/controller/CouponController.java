package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.CouponDto;
import com.springboot.cozyheaven.enums.CouponStatus;
import com.springboot.cozyheaven.enums.DiscountType;
import com.springboot.cozyheaven.model.Coupons;
import com.springboot.cozyheaven.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupon/add/")
    public void add(@Valid @RequestBody CouponDto couponDto){
        couponService.add(couponDto);
    }

    @GetMapping("/coupon/get/{id}")
    public Coupons getById(@PathVariable int id){
        return couponService.getById(id);
    }

    @GetMapping("/coupon/getAll/")
    public List<Coupons> getAll(){
        return couponService.getAll();
    }

    @PutMapping("/coupon/update/")
    public void update(@RequestParam(name="id",required = true)Integer id,
                       @RequestParam(name="code",required = false) String code,
                       @RequestParam(name = "discountType",required = false) DiscountType discountType,
                       @RequestParam(name="discountValue",required = false) Double discountValue,
                       @RequestParam(name = "expireDate",required = false) LocalDateTime expiryDate,
                       @RequestParam(name="status",required = false) CouponStatus status){
        couponService.update(code,discountType,discountValue,expiryDate,status,id);
    }

    @DeleteMapping("/coupon/delete/{id}")
    public void delete(@PathVariable int id){
        couponService.delete(id);
    }

}
