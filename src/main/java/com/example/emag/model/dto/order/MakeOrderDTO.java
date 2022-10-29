package com.example.emag.model.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class MakeOrderDTO {
    private List<ProductOrderDTO> products;
    private String firstName;
    private double totalPrice;
}
