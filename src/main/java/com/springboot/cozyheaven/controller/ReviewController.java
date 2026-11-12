package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.ReviewDto;
import com.springboot.cozyheaven.dto.response.ReviewRespDto;
import com.springboot.cozyheaven.mapper.ReviewMapper;
import com.springboot.cozyheaven.model.Review;
import com.springboot.cozyheaven.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review/add/{pid}")
    public void addReview(@Valid @RequestBody ReviewDto reviewDto,@PathVariable int pid, Principal principal){
        reviewService.addReview(reviewDto,pid,principal.getName());
    }



    @PutMapping("/review/edit/")
    public void update(@RequestParam(name = "rid",required = false)int rid,
                       @RequestParam(name="comments",required = false)String comments,
                       @RequestParam(name="ratings",required = false)Double ratings,
                       Principal principal){
        reviewService.update(rid,comments,ratings,principal.getName());
    }

    @GetMapping("/review/all/customer/")
    public List<ReviewRespDto> getAllReview(Principal principal){
        List<Review> reviews =  reviewService.getAllReview(principal.getName());
        List<ReviewRespDto> reviewRespDtos = new ArrayList<>();

        for(Review review1:reviews){
            reviewRespDtos.add(ReviewMapper.getMappedForDto(review1));
        }
        return reviewRespDtos;
    }

    @GetMapping("/review/all/hotel/")
    public List<Review> getAllReviewForHotel(Principal principal){
        return reviewService.getAllReviewForHotel(principal.getName());
    }

    @GetMapping("/review/all/")
    public List<Review> getAllReviews(){
        return reviewService.getAllReviews();
    }

}