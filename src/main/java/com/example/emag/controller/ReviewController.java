package com.example.emag.controller;

import com.example.emag.model.dto.product.ProductReviewDTO;
import com.example.emag.model.dto.review.ReviewForProductDTO;
import com.example.emag.model.dto.review.ReviewFromUsersDTO;
import com.example.emag.model.dto.review.ReviewRequestDTO;
import com.example.emag.model.dto.review.ReviewResponseDTO;
import com.example.emag.model.entities.Review;
import com.example.emag.model.repositories.ReviewRepository;
import com.example.emag.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
public class ReviewController extends AbstractController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ReviewResponseDTO addReview(@RequestBody ReviewRequestDTO dto) {
        //todo check if logged
        return reviewService.add(dto);
    }

    @GetMapping("reviews/users/{uid}")
    public List<ReviewFromUsersDTO> getAllReviewsFromUser (@PathVariable long uid){
        //todo check if admin or no
        return reviewService.getAllReviewsFromUser(uid);
    }

    @GetMapping("reviews/products/{pid}")
    public List<ReviewForProductDTO> getAllReviewsForProduct (@PathVariable long pid){
        //todo check if admin
        return reviewService.getAllReviewsForProduct(pid);
    }

    @DeleteMapping("reviews/{rid}")
    public ReviewResponseDTO deleteReview(@PathVariable long rid){
        //todo check if admin
        return reviewService.deleteReview(rid);
    }

    @PutMapping("reviews/{rid}")
    public ReviewResponseDTO editReview(@PathVariable long rid, HttpServletRequest request, @RequestBody ReviewRequestDTO dto){
//        logUser(request);
        long uid = getLoggedUserId(request);
        return reviewService.editReview(rid, uid, dto);
    }
        //todo add session to check if user
}
