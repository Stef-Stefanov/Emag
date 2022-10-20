package com.example.emag.model.repositories;

import com.example.emag.model.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAllByCategoryId(long categoryId, Sort sort);

}
