package com.example.emag.controller;

import com.example.emag.model.dto.ErrorDTO;
import com.example.emag.model.dto.product.LikedProductsDTO;
import com.example.emag.model.dto.product.ProductAddDTO;
import com.example.emag.model.dto.product.ProductDTO;
import com.example.emag.model.entities.Product;
import com.example.emag.model.entities.UserProductsInCart;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.repositories.ProductRepository;
import com.example.emag.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{pid}")
    public ProductDTO getProductById(@PathVariable long pid) {
        return productService.findById(pid);
    }
    @PostMapping("/products")
    public ProductDTO addProduct(@RequestBody ProductAddDTO p){
        //todo check if admin
        return productService.add(p);
    }

    @PostMapping("/products/{pid}/like")
    public LikedProductsDTO likeProduct(@PathVariable int pid, HttpServletRequest req){
        //todo check if logged
        int uid = getLoggedUserId(req);
        return productService.like(pid,uid);
    }

    @PostMapping("/products/{pid}/cart")
    public int addProductInCart(@PathVariable int pid, HttpServletRequest req, @RequestParam int quantity){
        //todo check if logged?
        int uid = getLoggedUserId(req);
        return productService.addToCart(pid, uid, quantity);
    }

    @DeleteMapping("/products/{pid}/cart")
    public int removeProductFromCart(@PathVariable int pid, HttpServletRequest req){
        //todo check if logged?
        //should i check if user is logged to add or remove product from cart
        int uid = getLoggedUserId(req);
        return productService.removeProductFromCart(pid,uid);
    }

    @PostMapping("/products/{pid}/image")
    public String addPicture(@RequestParam MultipartFile file, @PathVariable long pid){
        //todo check if admin
        return productService.addPicture(file,pid);
    }

    @PostMapping("products/{pid}")
    public ProductDTO editProduct(@PathVariable long pid,@RequestBody ProductAddDTO dto){
        //todo check if admin
        return productService.edit(pid, dto);
    }
    @DeleteMapping(value = "products/{pid}", headers = "password=dve")
    public void deleteProduct(@PathVariable long pid){
        //todo check if admin
        // check if ongoing purchases
        // cancel all purchases
        // what to return
        productService.deleteById(pid);
    }

//    @GetMapping("/products")
//    public List<Product> getAllProducts(@RequestParam long category,
//                                        @RequestParam(required = false) boolean sortByPrice,
//                                        @RequestParam(required = false) boolean desc) {
//        Sort sort = null;
//        if (sortByPrice) {
//            sort = Sort.by("regularPrice");
//            if (desc) {
//                sort = sort.descending();
//            }
//        }
//        return productRepository.findAllByCategoryId(category, sort);
//    }
//
//    //

}
