package com.example.emag.model.dto.product;

import com.example.emag.model.dto.category.CategoryDTO;
import com.example.emag.model.entities.ProductFeature;
import lombok.Data;

import java.util.List;

@Data
public class ProductWithFeaturesDTO {
    private Long id;
    private String name;
    private String description;
    private double regularPrice;
    private int quantity;
    private CategoryDTO category;
    private Long discountId;
    private List<ProductFeatureDTO> productFeatures;
}
