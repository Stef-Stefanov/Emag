package com.example.emag.model.dto.product;

import com.example.emag.model.dto.FeatureDTO;
import com.example.emag.model.entities.Feature;
import com.example.emag.model.entities.Product;
import com.example.emag.model.entities.ProductFeatureKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductFeatureDTO {
    private FeatureDTO feature;
    private String value;
}
