package com.example.emag.model.dto.review;

import com.example.emag.model.dto.product.ProductDTO;
import com.example.emag.model.dto.user.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class ReviewResponseDTO {
    private long id;
    private String text;
    private int rating;
    private LocalDateTime createdAt;
    private UserDTO user;
    private ProductDTO product;
}
