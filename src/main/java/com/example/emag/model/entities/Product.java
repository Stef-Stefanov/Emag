package com.example.emag.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import java.util.List;

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
    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;
    @Column
    private Long discountId;
    @ManyToMany(mappedBy = "likedProducts")
    private List<User> likes;
    @OneToMany(mappedBy = "product")
    private List<UserProductsInCart> productsInCart;
    @OneToMany(mappedBy = "productId")
    private List<ProductImage> productImages;
    @OneToMany(mappedBy = "product")
    private List<ProductFeature> productFeatures;


}
