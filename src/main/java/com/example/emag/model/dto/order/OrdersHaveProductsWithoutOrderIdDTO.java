package com.example.emag.model.dto.order;

import com.example.emag.model.dto.product.ProductDTO;
import com.example.emag.model.entities.OrderProductKey;
import com.example.emag.model.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersHaveProductsWithoutOrderIdDTO {
    private ProductDTO product;
    private int quantity;
}
