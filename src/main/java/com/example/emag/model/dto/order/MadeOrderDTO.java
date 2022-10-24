package com.example.emag.model.dto.order;

import com.example.emag.model.dto.product.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class MadeOrderDTO {
    private List<ProductOrderDTO> products;
    private String firstName;
    private double totalPrice;
}
