package com.example.emag.controller;

import com.example.emag.model.entities.Review;
import com.example.emag.model.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository repository;

    @PostMapping("/reviews")
    public Review addReview(@RequestBody Review r) {
        //todo set valid date from json
        repository.save(r);
        return r;
    }
}
