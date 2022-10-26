package com.example.emag.model.dto.cart;

import com.example.emag.model.dto.product.ProductDTO;
import com.example.emag.model.entities.Product;

import lombok.Data;


@Data
public class UserHasProductsInCardWithoutUserIdDTO {

    private ProductDTO product;
    private int quantity;
}
