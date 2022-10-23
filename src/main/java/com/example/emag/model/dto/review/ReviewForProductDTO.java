package com.example.emag.model.dto.review;

import com.example.emag.model.dto.user.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewForProductDTO {
    private long id;
    private String text;
    private int rating;
    private LocalDateTime createdAt;
    private UserDTO user;
}
