package com.example.emag.model.dto.user;

import com.example.emag.model.dto.product.ProductDTO;
import lombok.Data;

import java.util.List;
@Data
public class UserFavoritesDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private List<ProductDTO> likedProducts;
}
