package com.example.emag.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
//@Table(name = "user_products_in_cart")
public class UserProductsInCart {
    @EmbeddedId
    private UserProductsInCartKey id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;

}
