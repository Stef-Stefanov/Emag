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

    @PostMapping("/reviews/{pid}")
    public ReviewResponseDTO addReview(@RequestBody ReviewRequestDTO dto, HttpServletRequest req,
                                       @PathVariable long pid) {
        long uid = getLoggedUserId(req);
        return reviewService.add(dto, uid, pid);
    }

    @GetMapping("/users/{uid}/reviews")
    public List<ReviewFromUsersDTO> getAllReviewsFromUser (@PathVariable long uid){
        return reviewService.getAllReviewsFromUser(uid);
    }

    @GetMapping("/products/{pid}/reviews")
    public List<ReviewForProductDTO> getAllReviewsForProduct (@PathVariable long pid){
        return reviewService.getAllReviewsForProduct(pid);
    }

    @DeleteMapping("/reviews/{rid}")
    public ReviewResponseDTO deleteReview(@PathVariable long rid, HttpServletRequest req){
        long uid = getLoggedUserId(req);
        return reviewService.deleteReview(rid, uid);
    }

    @PutMapping("/reviews/{rid}")
    public ReviewResponseDTO editReview(@PathVariable long rid, HttpServletRequest request,
                                        @RequestBody ReviewRequestDTO dto){
        checkIfLogged(request);
        long uid = getLoggedUserId(request);
        return reviewService.editReview(rid, uid, dto);
    }
}
