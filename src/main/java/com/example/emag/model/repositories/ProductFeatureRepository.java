package com.example.emag.model.repositories;

import com.example.emag.model.entities.ProductFeature;
import com.example.emag.model.entities.ProductFeatureKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFeatureRepository extends JpaRepository<ProductFeature, ProductFeatureKey> {
}
