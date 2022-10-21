package com.example.emag.model.dto.product;

import com.example.emag.model.entities.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LikedProductsDTO {
    private List<ProductDTO> likedProducts;
}
