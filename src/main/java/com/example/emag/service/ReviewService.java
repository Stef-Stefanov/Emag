package com.example.emag.service;

import com.example.emag.model.dto.review.ReviewForProductDTO;
import com.example.emag.model.dto.review.ReviewFromUsersDTO;
import com.example.emag.model.dto.review.ReviewRequestDTO;
import com.example.emag.model.dto.review.ReviewResponseDTO;
import com.example.emag.model.entities.Product;
import com.example.emag.model.entities.Review;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService extends AbstractService{


    public ReviewResponseDTO add(ReviewRequestDTO dto) {
        validateDTO(dto);
        User user = getUserById(dto.getUserId());
        Product product = getProductById(dto.getProductId());
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
        review.setRating(dto.getRating());
        review.setText(dto.getText());
        return modelMapper.map(reviewRepository.save(review), ReviewResponseDTO.class);
    }

    private void validateDTO(ReviewRequestDTO dto){
        if(dto.getRating() > 5 || dto.getRating() < 1){
            throw new BadRequestException("Invalid rating value");
        }
        if(dto.getText().strip().length() < 1){
            throw new BadRequestException("Empty review");
        }
    }

    public List<ReviewFromUsersDTO> getAllReviewsFromUser(long uid) {
        User user = getUserById(uid);
        List<Review> userReviews = user.getReviews();
        return userReviews.stream().map(review -> modelMapper.map
                (review, ReviewFromUsersDTO.class)).collect(Collectors.toList());

    }

    public List<ReviewForProductDTO> getAllReviewsForProduct(long pid) {
        Product product = getProductById(pid);
        List<Review> productReviews = product.getReviews();
        //todo can add new userdto with id and name
        return productReviews.stream().map(review -> modelMapper.map(review, ReviewForProductDTO.class))
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO deleteReview(long rid) {
        Review review = getReviewById(rid);
        ReviewResponseDTO dto = modelMapper.map(review, ReviewResponseDTO.class);
        reviewRepository.deleteById(rid);
        return dto;
    }

    public ReviewResponseDTO editReview(long rid, long uid, ReviewRequestDTO dto) {
        // check if this user has review for the current product
        //todo fix
        validateDTO(dto);
        User user = getUserById(uid);
        Review review = getReviewById(rid);
        List<Review> reviews = user.getReviews();
        if(!checkIfReviewIsFromThisUser(reviews, rid)) {
            throw new BadRequestException("You didn't write a review for this product");
        }
        review.setText(dto.getText());
        review.setRating(dto.getRating());
        review.setCreatedAt(LocalDateTime.now());
        return modelMapper.map(reviewRepository.save(review), ReviewResponseDTO.class);
    }

    private boolean checkIfReviewIsFromThisUser(List<Review> reviews, long rid) {
        for(Review review : reviews){
            if(review.getId() == rid) {
                return true;
            }
        }
        return false;
    }
}
