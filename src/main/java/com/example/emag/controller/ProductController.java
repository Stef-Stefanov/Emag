package com.example.emag.controller;

import com.example.emag.model.dto.ErrorDTO;
import com.example.emag.model.entities.Product;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorDTO handleNotFound(Exception ex) {
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg(ex.getMessage());
        dto.setStatus(HttpStatus.NOT_FOUND.value());
        dto.setTime(LocalDateTime.now());
        return dto;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts(@RequestParam long category,
                                        @RequestParam(required = false) boolean sortByPrice,
                                        @RequestParam(required = false) boolean desc) {
        Sort sort = null;
        if (sortByPrice) {
            sort = Sort.by("regularPrice");
            if (desc) {
                sort = sort.descending();
            }
        }
        return productRepository.findAllByCategoryId(category, sort);
    }

    //
    @DeleteMapping(value = "products/{id}", headers = "password=dve")
    public void deleteProduct(@PathVariable long id) {
        // todo check if admin
        // check if ongoing purchases
        // cancel all purchases
        // check if exists
        productRepository.deleteById(id);
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product p) {
        // todo fix relationships because category cannot be null
        // validation
        productRepository.save(p);
        return p;
    }

}
