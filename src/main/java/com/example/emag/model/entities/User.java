package com.example.emag.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    @Column
    private int isSubscribed;
    @Column
    private int isAdmin;
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
