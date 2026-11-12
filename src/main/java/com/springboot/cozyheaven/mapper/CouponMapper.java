package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.CouponDto;
import com.springboot.cozyheaven.model.Coupons;

public class CouponMapper {

    public static Coupons getMappedByCoupon(CouponDto couponDto){

        Coupons coupons = new Coupons();
        coupons.setCode(couponDto.code());
        coupons.setStatus(couponDto.status());
        coupons.setDiscountType(couponDto.discountType());
        coupons.setExpiryDate(couponDto.expiryDate());
        coupons.setDiscountValue(couponDto.discountValue());
        return coupons;

    }

}
