package com.example.emag.model.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDTO {
    private String text;
    private int rating;
    private long userId;
    private long productId;
}
