package com.example.emag.service;

import com.example.emag.model.dto.review.ReviewRequestDTO;
import com.example.emag.model.dto.review.ReviewResponseDTO;
import com.example.emag.model.entities.Product;
import com.example.emag.model.entities.Review;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService extends AbstractService{


    public ReviewResponseDTO add(ReviewRequestDTO r) {
        validateDTO(r);
        User user = getUserById(r.getUserId());
        Product product = getProductById(r.getProductId());
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
        review.setRating(r.getRating());
        review.setText(r.getText());
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
}
