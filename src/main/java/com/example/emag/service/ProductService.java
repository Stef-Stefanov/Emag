package com.example.emag.service;

import com.example.emag.model.dto.product.LikedProductsDTO;
import com.example.emag.model.dto.product.ProductAddDTO;
import com.example.emag.model.dto.product.ProductDTO;
import com.example.emag.model.entities.*;
import com.example.emag.model.exceptions.BadRequestException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class ProductService extends AbstractService{

    public ProductDTO findById(Long id){
        Product p = getProductById(id);
        return modelMapper.map(p,ProductDTO.class);
    }

    public void deleteById(long pid) {
        getProductById(pid);
        productRepository.deleteById(pid);
    }

    public ProductDTO add(ProductAddDTO p) {
        validateProduct(p);
        Category category = getCategoryById(p);
        Product product = new Product();
        product.setName(p.getName());
        product.setRegularPrice(p.getRegularPrice());
        product.setDescription(p.getDescription());
        product.setRegularPrice(p.getRegularPrice());
        product.setQuantity(p.getQuantity());
        product.setCategory(category);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    public LikedProductsDTO like(int pid, int uid) {
        User u = getUserById(uid);
        Product p = getProductById(pid);
        if(u.getLikedProducts().contains(p)){
            u.getLikedProducts().remove(p);
        }else{
            u.getLikedProducts().add(p);
        }
        userRepository.save(u);
        return modelMapper.map(u,LikedProductsDTO.class);
    }

    public int addToCart(long pid, long uid, int quantity) {
        validateQuantity(quantity);
        UserProductsInCartKey pk = new UserProductsInCartKey();
        pk.setUserId(uid);
        pk.setProductId(pid);
        User u = getUserById(uid);
        Product p = getProductById(pid);
        UserProductsInCart productsInCart = new UserProductsInCart();
        productsInCart.setProduct(p);
        productsInCart.setUser(u);
        productsInCart.setId(pk);
        productsInCart.setQuantity(quantity);
        userCartRepository.save(productsInCart);
        return productsInCart.getQuantity();
    }

    public String addPicture(MultipartFile file, long pid) {
        Product product = getProductById(pid);
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = "uploads" + File.separator + System.nanoTime() + "." + extension;
        File f = new File(name);
        if(!f.exists()){
            try {
                Files.copy(file.getInputStream(),f.toPath());
            } catch (IOException e) {
                throw new BadRequestException("File already exists");
            }
        }
        ProductImage image = new ProductImage();
        image.setUrl(name);
        image.setProductId(product);
        product.getProductImages().add(image);
        productImageRepository.save(image);
        return image.getUrl();
    }

    public ProductDTO edit(long pid, ProductAddDTO dto) {
        Product p = getProductById(pid);
        validateProduct(dto);
        Category category = getCategoryById(dto);
        p.setCategory(category);
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setQuantity(dto.getQuantity());
        p.setRegularPrice(dto.getRegularPrice());
        return modelMapper.map(productRepository.save(p), ProductDTO.class);
    }

    public int removeProductFromCart(long pid, long uid) {
        UserProductsInCartKey pk = new UserProductsInCartKey();
        pk.setProductId(pid);
        pk.setUserId(uid);
        userCartRepository.deleteById(pk);
        //todo what to return when removing product
        return 222;
    }

    private void validateQuantity(int quantity) {
        if(quantity <= 0){
            throw new BadRequestException ("Quantity must be positive");
        }
        if(quantity > 100){
            throw new BadRequestException ("Max quantity is 100");
        }
    }

    private void validateProduct(ProductAddDTO p) {
        if(p.getName().length() > 255 || p.getName().strip().length() < 1){
            throw new BadRequestException("Product name size is not valid");
        }
        if(p.getRegularPrice() <= 0){
            throw new BadRequestException("Price must be positive");
        }
        if(p.getQuantity() <= 0){
            throw new BadRequestException("Quantity must be positive");
        }
        if(p.getQuantity() > 100){
            throw new BadRequestException("Max quantity is 100");
        }
    }


}
