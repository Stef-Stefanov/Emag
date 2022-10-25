package com.example.emag.model.dto.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQueryDTO {
    private long id;
    private String name;
    private String description;
    private Double regularPrice;
    private int quantity;
    private long categoryId;
    private long discountId;
}
