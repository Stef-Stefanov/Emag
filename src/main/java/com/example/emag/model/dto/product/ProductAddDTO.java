package com.example.emag.model.dto.product;

import lombok.Data;

import javax.persistence.Column;

@Data
public class ProductAddDTO {
    private String name;
    private String description;
    private double regularPrice;
    private int quantity;
    @Column(name = "category_id")
    private Long categoryId;

}
