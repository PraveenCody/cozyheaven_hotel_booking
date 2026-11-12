package com.springboot.cozyheaven;

import com.springboot.cozyheaven.dto.request.ReviewDto;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.ReviewMapper;
import com.springboot.cozyheaven.model.*;
import com.springboot.cozyheaven.repository.ReviewRepository;
import com.springboot.cozyheaven.service.PaymentService;
import com.springboot.cozyheaven.service.ReviewService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private ReviewService reviewService;


    @Test
    public void addReviewTest() {

        User user = new User();
        user.setUsername("jesse");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setId(1);
        booking.setCustomer(customer);

        Payment payment = new Payment();
        payment.setBooking(booking);

        Hotel hotel = new Hotel();

        ReviewDto reviewDto = mock(ReviewDto.class);
        Review review = new Review();

        when(paymentService.getById(1)).thenReturn(payment);
        when(reviewRepository.getHotelIdByBookingId(1)).thenReturn(hotel);

        MockedStatic<ReviewMapper> mapper =
                mockStatic(ReviewMapper.class);

        mapper.when(() -> ReviewMapper.getMappedforReview(reviewDto))
                .thenReturn(review);

        reviewService.addReview(reviewDto, 1, "jesse");

        assertEquals(payment, review.getPayment());
        assertEquals(customer, review.getCustomer());
        assertEquals(hotel, review.getHotel());
        assertNotNull(review.getReviewDate());
        assertNotNull(review.getReviewUpdatedDate());

        verify(reviewRepository, times(1)).save(review);

        mapper.close();
    }


    @Test
    public void addReviewOwnershipExceptionTest() {

        User user = new User();
        user.setUsername("gus fring");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setId(1);
        booking.setCustomer(customer);

        Payment payment = new Payment();
        payment.setBooking(booking);

        ReviewDto reviewDto = mock(ReviewDto.class);
        Review review = new Review();

        when(paymentService.getById(1)).thenReturn(payment);

        MockedStatic<ReviewMapper> mapper =
                mockStatic(ReviewMapper.class);

        mapper.when(() -> ReviewMapper.getMappedforReview(reviewDto))
                .thenReturn(review);

        assertThrows(OwnershipException.class,
                () -> reviewService.addReview(reviewDto, 1, "jesse"));

        verify(reviewRepository, never()).save(any(Review.class));

        mapper.close();
    }


    @Test
    public void addReviewHotelNotFoundTest() {

        User user = new User();
        user.setUsername("jesse");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setId(1);
        booking.setCustomer(customer);

        Payment payment = new Payment();
        payment.setBooking(booking);

        ReviewDto reviewDto = mock(ReviewDto.class);
        Review review = new Review();

        when(paymentService.getById(1)).thenReturn(payment);
        when(reviewRepository.getHotelIdByBookingId(1)).thenReturn(null);

        MockedStatic<ReviewMapper> mapper =
                mockStatic(ReviewMapper.class);

        mapper.when(() -> ReviewMapper.getMappedforReview(reviewDto))
                .thenReturn(review);

        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.addReview(reviewDto, 1, "jesse"));

        verify(reviewRepository, never()).save(any(Review.class));

        mapper.close();
    }


    @Test
    public void getByIdTest() {

        User user = new User();
        user.setUsername("jesse");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);

        Payment payment = new Payment();
        payment.setBooking(booking);

        Review review = new Review();
        review.setPayment(payment);

        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(review));

        Review result = reviewService.getById("jesse", 1);

        assertNotNull(result);
        assertEquals(review, result);

        verify(reviewRepository, times(1)).findById(1);
    }


    @Test
    public void getByIdNotFoundTest() {

        when(reviewRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.getById("jesse", 1));

        verify(reviewRepository, times(1)).findById(1);
    }


    @Test
    public void getByIdOwnershipExceptionTest() {

        User user = new User();
        user.setUsername("gus fring");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);

        Payment payment = new Payment();
        payment.setBooking(booking);

        Review review = new Review();
        review.setPayment(payment);

        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(review));

        assertThrows(OwnershipException.class,
                () -> reviewService.getById("jesse", 1));

        verify(reviewRepository, times(1)).findById(1);
    }


    @Test
    public void updateTest() {

        User user = new User();
        user.setUsername("jesse");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);

        Payment payment = new Payment();
        payment.setBooking(booking);

        Review review = new Review();
        review.setPayment(payment);
        review.setComment("Good");
        review.setRating(3.0);

        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(review));

        reviewService.update(1, "Excellent", 4.5, "jesse");

        assertEquals("Excellent", review.getComment());
        assertEquals(4.5, review.getRating());
        assertNotNull(review.getReviewUpdatedDate());

        verify(reviewRepository, times(1)).save(review);
    }


    @Test
    public void updateWithoutChangesTest() {

        User user = new User();
        user.setUsername("jesse");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);

        Payment payment = new Payment();
        payment.setBooking(booking);

        Review review = new Review();
        review.setPayment(payment);
        review.setComment("Good");
        review.setRating(3.0);

        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(review));

        reviewService.update(1, "", 0.0, "jesse");

        assertEquals("Good", review.getComment());
        assertEquals(3.0, review.getRating());

        verify(reviewRepository, times(1)).save(review);
    }


    @Test
    public void updateNotFoundTest() {

        when(reviewRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.update(1, "Excellent", 4.5, "jesse"));

        verify(reviewRepository, never()).save(any(Review.class));
    }


    @Test
    public void updateOwnershipExceptionTest() {

        User user = new User();
        user.setUsername("gus fring");

        Customer customer = new Customer();
        customer.setUser(user);

        Bookings booking = new Bookings();
        booking.setCustomer(customer);

        Payment payment = new Payment();
        payment.setBooking(booking);

        Review review = new Review();
        review.setPayment(payment);

        when(reviewRepository.findById(1))
                .thenReturn(Optional.of(review));

        assertThrows(OwnershipException.class,
                () -> reviewService.update(1, "Excellent", 4.5, "jesse"));

        verify(reviewRepository, never()).save(any(Review.class));
    }


    @Test
    public void getAllReviewTest() {

        Review review1 = new Review();
        Review review2 = new Review();

        List<Review> reviews = List.of(review1, review2);

        when(reviewRepository.getAllReviewByCustomer("jesse"))
                .thenReturn(reviews);

        List<Review> result = reviewService.getAllReview("jesse");

        assertEquals(2, result.size());
        assertEquals(reviews, result);

        verify(reviewRepository, times(1))
                .getAllReviewByCustomer("jesse");
    }





    @Test
    public void getAllReviewsTest() {

        Review review1 = new Review();
        Review review2 = new Review();

        List<Review> reviews = List.of(review1, review2);

        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> result = reviewService.getAllReviews();

        assertEquals(2, result.size());
        assertEquals(reviews, result);

        verify(reviewRepository, times(1)).findAll();
    }


    @Test
    public void getAllReviewsEmptyTest() {

        when(reviewRepository.findAll()).thenReturn(List.of());

        List<Review> result = reviewService.getAllReviews();

        assertTrue(result.isEmpty());

        verify(reviewRepository, times(1)).findAll();
    }
}