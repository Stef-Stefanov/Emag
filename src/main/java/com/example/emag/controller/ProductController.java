package com.example.emag.controller;

import com.example.emag.model.dto.ErrorDTO;
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
        return productService.add(p);
    }

    @PostMapping("/products/{pid}/like")
    public ProductDTO likeProduct(@PathVariable int pid, HttpServletRequest req){
        int uid = getLoggedUserId(req);
        return productService.like(pid,uid);
    }

    @PostMapping("/products/{pid}/cart")
    public int addProductInCart(@PathVariable int pid, HttpServletRequest req){
        int uid = getLoggedUserId(req);
        return productService.addToCart(pid,uid);
    }
    @PostMapping("/products/{pid}/image")
    public String addPicture(@RequestParam MultipartFile file, @PathVariable long pid){
        return productService.addPicture(file,pid);
    }
    @PostMapping("products/{pid}")
    public ProductDTO editProduct(@PathVariable long pid,@RequestBody ProductAddDTO dto){
        return productService.edit(pid, dto);
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
//    @DeleteMapping(value = "products/{id}", headers = "password=dve")
//    public void deleteProduct(@PathVariable long id) {
//        // todo check if admin
//        // check if ongoing purchases
//        // cancel all purchases
//        // check if exists
////        productRepository.deleteById(id);
//    }

}
