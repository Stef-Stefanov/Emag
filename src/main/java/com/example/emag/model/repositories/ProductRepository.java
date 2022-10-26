package com.example.emag.model.repositories;

import com.example.emag.model.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAll(Sort sort);
    List<Product> findAllByNameContainingIgnoreCase(String word);

}
