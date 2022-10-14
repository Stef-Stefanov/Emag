package com.example.emag.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private double regularPrice;
    @Column
    private int quantity;
    @Column
    private long categoryId;
    @Column
    private Long discountId;



}
