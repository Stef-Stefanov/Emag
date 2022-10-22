package com.example.emag.model.dto.product;

import com.example.emag.model.dto.category.CategoryDTO;
import com.example.emag.model.dto.discount.DiscountResponseDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double regularPrice;
    private int quantity;
    private CategoryDTO category;
    private DiscountResponseDTO discount;
}
