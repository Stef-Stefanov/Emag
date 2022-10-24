package com.example.emag.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private double price;
    @Column
    private LocalDateTime createdAt;
    //todo fix this relation with user one to many
    @Column
    private long userId;
    @OneToMany(mappedBy = "order")
    private List<OrderProduct> productsInOrder;
}
