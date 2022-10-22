package com.example.emag.model.dto.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DiscountResponseDTO {
    private long id;
    private int discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime expireDate;
}
