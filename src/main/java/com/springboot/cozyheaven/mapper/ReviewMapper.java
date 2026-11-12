package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.ReviewDto;
import com.springboot.cozyheaven.dto.response.ReviewRespDto;
import com.springboot.cozyheaven.model.Review;

public class ReviewMapper {

    public static Review getMappedforReview(ReviewDto reviewDto){

        Review review= new Review();
        review.setRating(reviewDto.rating());
        review.setComment(reviewDto.comments());
        return review;
    }

    public static ReviewRespDto getMappedForDto(Review review){

        return new ReviewRespDto(review.getReviewId(),review.getPayment().getId(),review.getHotel().getHotelName(),review.getRating()
                ,review.getComment(),review.getReviewDate(),review.getReviewUpdatedDate());

    }

}
