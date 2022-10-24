package com.example.emag.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "orders_have_products")
@Data
public class OrderProduct {
    @EmbeddedId
    private OrderProductKey id;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;

}
