package com.example.emag.controller;

import com.example.emag.model.dto.review.ReviewRequestDTO;
import com.example.emag.model.dto.review.ReviewResponseDTO;
import com.example.emag.model.entities.Review;
import com.example.emag.model.repositories.ReviewRepository;
import com.example.emag.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ReviewController extends AbstractController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ReviewResponseDTO addReview(@RequestBody ReviewRequestDTO r) {
        //todo set valid date from json
        return reviewService.add(r);
    }
}
