package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.ReviewDto;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.ReviewMapper;
import com.springboot.cozyheaven.model.Customer;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.model.Payment;
import com.springboot.cozyheaven.model.Review;
import com.springboot.cozyheaven.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PaymentService paymentService;

    public void addReview(ReviewDto reviewDto, int pid,String name) {

        Review review = ReviewMapper.getMappedforReview(reviewDto);
        Payment payment = paymentService.getById(pid);
        Customer customer = payment.getBooking().getCustomer();
        Hotel hotel= reviewRepository.getHotelIdByBookingId(payment.getBooking().getId());

        if(!payment.getBooking().getCustomer().getUser().getUsername().equals(name))
            throw new OwnershipException("Auth users can perform task");

        if(hotel==null)
            throw new ResourceNotFoundException("Hotel id not found");

        int count = reviewRepository.getCountOfHotelReview(hotel.getHotelId());
        double rate = hotel.getOverAllRating();
        double newRate = ((rate * count) + review.getRating()) / (count + 1);

        hotel.setOverAllRating(newRate);
        review.setCustomer(customer);
        review.setHotel(hotel);
        review.setPayment(payment);
        review.setReviewDate(LocalDateTime.now());
        review.setReviewUpdatedDate(LocalDateTime.now());
        reviewRepository.save(review);
    }

//    public double updateRating(int id){
//
//        List<Review> reviewsOfHotel = reviewRepository.getAllReviewByHotel(id);
//        double rate = 0;
//
//        for(Review review:reviewsOfHotel){
//            rate+=review.getRating();
//        }
//
//        return rate/reviewsOfHotel.size();
//    }


    public Review getById(String name,int id){

        Optional<Review> review = reviewRepository.findById(id);

        if(review.isEmpty())
            throw new ResourceNotFoundException("Review id not");

        if(!review.get().getPayment().getBooking().getCustomer().getUser().getUsername().equals(name))
            throw new OwnershipException("Need Auth user to perform task");

        return review.get();
    }


    public void update(int id,String comments,Double rating, String name){

        Review review = getById(name,id);

        if (comments != null && !comments.isEmpty())
            review.setComment(comments);

        if (rating != null && rating > 0) {
            review.setRating(rating);
        }

        review.setReviewUpdatedDate(LocalDateTime.now());
        reviewRepository.save(review);

    }

    public List<Review> getAllReview(String name) {
        return reviewRepository.getAllReviewByCustomer(name);
    }

    public List<Review> getAllReviewForHotel(String name) {
        return reviewRepository.getAllReviewByHotel(name);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}
