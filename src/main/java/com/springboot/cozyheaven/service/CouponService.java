package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.CouponDto;
import com.springboot.cozyheaven.enums.CouponStatus;
import com.springboot.cozyheaven.enums.DiscountType;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.CouponMapper;
import com.springboot.cozyheaven.model.Coupons;
import com.springboot.cozyheaven.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public void add(CouponDto couponDto) {
        couponRepository.save(CouponMapper.getMappedByCoupon(couponDto));
    }

    public List<Coupons> getAll() {
        return couponRepository.findAll();
    }

    public Coupons getById(int id){

        Optional<Coupons> coupons = couponRepository.findById(id);
        if(coupons.isEmpty())
            throw new ResourceNotFoundException("Coupon id not found");

        return coupons.get();
    }

    public void update(String code, DiscountType discountType, Double discountValue, LocalDateTime expiryDate, CouponStatus status, int id) {

        Coupons coupons = getById(id);

        if(code != null || !code.isBlank())
            coupons.setCode(code);
        if(status != null)
            coupons.setStatus(status);
        if (discountValue != null) {
            coupons.setDiscountValue(discountValue);
        }
        if (expiryDate != null)
            coupons.setExpiryDate(expiryDate);
        if(discountType != null)
            coupons.setDiscountType(discountType);

        couponRepository.save(coupons);

    }

    public void delete(int id){

        Coupons coupons = getById(id);

        if(coupons == null)
            throw new ResourceNotFoundException("Id not found");

        couponRepository.deleteById(id);

    }

    public Coupons getCouponByCode(String code) {
        return couponRepository.getByCouponCode(code);
    }


}
