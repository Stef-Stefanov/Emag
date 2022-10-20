package com.example.emag.model.repositories;

import com.example.emag.model.entities.UserProductsInCart;
import com.example.emag.model.entities.UserProductsInCartKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCartRepository extends JpaRepository<UserProductsInCart, UserProductsInCartKey> {
}
