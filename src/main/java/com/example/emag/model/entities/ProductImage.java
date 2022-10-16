package com.example.emag.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "products_images")
@Data
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String url;
    @Column
    private long productId;
}
