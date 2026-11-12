package com.springboot.cozyheaven;

import com.springboot.cozyheaven.dto.request.CouponDto;
import com.springboot.cozyheaven.enums.CouponStatus;
import com.springboot.cozyheaven.enums.DiscountType;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.CouponMapper;
import com.springboot.cozyheaven.model.Coupons;
import com.springboot.cozyheaven.repository.CouponRepository;
import com.springboot.cozyheaven.service.CouponService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    public void testAdd() {

        CouponDto couponDto = new CouponDto(
                "HEISENBERG",
                DiscountType.PERCENT,
                20.0,
                LocalDateTime.now().plusDays(10),
                CouponStatus.ACTIVE
        );

        Coupons coupon = new Coupons();
        coupon.setCode("HEISENBERG");

        MockedStatic<CouponMapper> mapper = mockStatic(CouponMapper.class);

        mapper.when(() -> CouponMapper.getMappedByCoupon(couponDto)).thenReturn(coupon);

        couponService.add(couponDto);

        verify(couponRepository, times(1)).save(coupon);

    }

    @Test
    public void testGetAll() {

        List<Coupons> list = new ArrayList<>();

        Coupons coupon1 = new Coupons();
        coupon1.setCode("HEISENBERG");

        Coupons coupon2 = new Coupons();
        coupon2.setCode("POLLos");

        list.add(coupon1);
        list.add(coupon2);

        when(couponRepository.findAll()).thenReturn(list);

        List<Coupons> coupons = couponService.getAll();

        assertEquals(list, coupons);
        assertEquals(2, coupons.size());

        verify(couponRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {

        Coupons coupon = new Coupons();
        coupon.setCode("HEISENBERG");

        when(couponRepository.findById(1))
                .thenReturn(Optional.of(coupon));

        Coupons result = couponService.getById(1);

        assertEquals(coupon, result);

        verify(couponRepository, times(1)).findById(1);
    }

    @Test
    public void testGetByIdException() {

        when(couponRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> couponService.getById(1));

        verify(couponRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdate() {

        Coupons coupon = new Coupons();
        coupon.setCode("HEISENBERG");
        coupon.setDiscountType(DiscountType.PERCENT);
        coupon.setDiscountValue(10.0);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(5));
        coupon.setStatus(CouponStatus.ACTIVE);

        LocalDateTime newExpiryDate = LocalDateTime.now().plusDays(30);

        when(couponRepository.findById(1))
                .thenReturn(Optional.of(coupon));

        couponService.update(
                "LOS POLLOS",
                DiscountType.FIXED,
                50.0,
                newExpiryDate,
                CouponStatus.ACTIVE,
                1
        );

        assertEquals("LOS POLLOS", coupon.getCode());
        assertEquals(DiscountType.FIXED, coupon.getDiscountType());
        assertEquals(50.0, coupon.getDiscountValue());
        assertEquals(newExpiryDate, coupon.getExpiryDate());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());

        verify(couponRepository, times(1)).save(coupon);
    }

    @Test
    public void testUpdateException() {

        when(couponRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> couponService.update(
                        "HEISENBERG",
                        DiscountType.PERCENT,
                        20.0,
                        LocalDateTime.now().plusDays(10),
                        CouponStatus.ACTIVE,
                        1
                ));

        verify(couponRepository, never()).save(any(Coupons.class));
    }

    @Test
    public void testDelete() {

        Coupons coupon = new Coupons();
        coupon.setCode("HEISENBERG");

        when(couponRepository.findById(1))
                .thenReturn(Optional.of(coupon));

        couponService.delete(1);

        verify(couponRepository, times(1)).findById(1);
        verify(couponRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteException() {

        when(couponRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> couponService.delete(1));

        verify(couponRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testGetCouponByCode() {

        Coupons coupon = new Coupons();
        coupon.setCode("HEISENBERG");

        when(couponRepository.getByCouponCode("HEISENBERG"))
                .thenReturn(coupon);

        Coupons result = couponService.getCouponByCode("HEISENBERG");

        assertEquals(coupon, result);

        verify(couponRepository, times(1))
                .getByCouponCode("HEISENBERG");
    }

}