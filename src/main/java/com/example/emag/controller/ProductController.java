package com.example.emag.controller;

import com.example.emag.model.entities.Product;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable long id){
        Optional<Product> product = productRepository.findById(id);
        System.out.println(product);
        if(product.isPresent()){
            return product.get();
        }
        else{
            throw new NotFoundException("Product not found");
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String handleNotFound(Exception ex){
        return "Sorry there is an error " + ex.getMessage();
    }

    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
//
    @DeleteMapping("products/{id}")
    public void deleteProduct(@PathVariable long id){
        productRepository.deleteById(id);
    }

}
