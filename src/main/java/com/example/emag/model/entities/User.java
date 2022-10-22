package com.example.emag.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String createdAt;
    @Column(name = "is_subscribed")
    private boolean isSubscribed;
    @Column(name = "is_admin")
    private boolean isAdmin;
    @Column
    private String phoneNumber;
    @Column
    private String birthDate;


    @ManyToMany
    @JoinTable(name = "users_like_products",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> likedProducts;
    @OneToMany(mappedBy = "user")
    private List<UserProductsInCart> productsInCart;

}
