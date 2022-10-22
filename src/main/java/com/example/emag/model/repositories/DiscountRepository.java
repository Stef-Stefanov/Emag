package com.example.emag.model.repositories;

import com.example.emag.model.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount,Long> {

}
