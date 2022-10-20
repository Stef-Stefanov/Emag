package com.example.emag.model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Data
public class UserProductsInCartKey implements Serializable {
    @Column(name = "user_id")
    Long userId;
    @Column(name = "product_id")
    Long productId;
}
