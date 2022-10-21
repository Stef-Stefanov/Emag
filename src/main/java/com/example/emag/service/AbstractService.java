package com.example.emag.service;

import com.example.emag.model.dto.product.ProductAddDTO;
import com.example.emag.model.entities.Category;
import com.example.emag.model.entities.Feature;
import com.example.emag.model.entities.Product;
import com.example.emag.model.entities.User;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService {
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserCartRepository userCartRepository;
    @Autowired
    protected ProductImageRepository productImageRepository;
    @Autowired
    protected FeatureRepository featureRepository;
    @Autowired
    protected ProductFeatureRepository productFeatureRepository;

//    protected Product findProductById(long id) {
//        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
//    }

    protected Category getCategoryById(ProductAddDTO p) {
        return categoryRepository.findById(p.getCategoryId()).orElseThrow
                (() -> new NotFoundException("Category not found"));
    }

    protected User getUserById(long id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    protected Product getProductById(long pid){
        return productRepository.findById(pid).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    protected Feature getFeatureById(long id){
        return featureRepository.findById(id).orElseThrow(() -> new NotFoundException("Feature not found"));
    }

}
