package com.example.emag.model.dto.review;

import com.example.emag.model.dto.product.ProductDTO;
import com.example.emag.model.dto.product.ProductReviewDTO;
import com.example.emag.model.dto.user.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewFromUsersDTO {
    private long id;
    private String text;
    private int rating;
    private LocalDateTime createdAt;
    private ProductReviewDTO product;
}
