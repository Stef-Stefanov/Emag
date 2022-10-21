package com.example.emag.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ProductFeatureKey implements Serializable {
    @Column(name = "product_id")
    Long productId;
    @Column(name = "feature_id")
    Long featureId;
}
