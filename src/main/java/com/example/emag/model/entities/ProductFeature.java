package com.example.emag.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "products_have_features")
@Getter
@Setter
@NoArgsConstructor
public class ProductFeature {
    @EmbeddedId
    private ProductFeatureKey id;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @MapsId("featureId")
    @JoinColumn(name = "feature_id")
    private Feature feature;
    private String value;
}
