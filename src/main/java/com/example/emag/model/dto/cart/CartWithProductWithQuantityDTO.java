package com.example.emag.model.dto.cart;

import com.example.emag.model.dto.product.ProductDTO;

import lombok.Data;


@Data
public class CartWithProductWithQuantityDTO {

    private ProductDTO product;
    private int quantity;
}
