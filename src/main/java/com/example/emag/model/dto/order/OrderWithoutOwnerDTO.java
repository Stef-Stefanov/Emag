package com.example.emag.model.dto.order;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderWithoutOwnerDTO {
    private double price;
    private LocalDateTime createdAt;
    private List<OrdersHaveProductsWithoutOrderIdDTO> productsInOrder;
}
