package com.example.emag.controller;

import com.example.emag.model.dto.FeatureDTO;
import com.example.emag.model.dto.order.ProductOrderDTO;
import com.example.emag.model.dto.product.*;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Pageable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
public class ProductController extends AbstractController{

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{pid}")
    public ProductWithFeaturesDTO getProductById(@PathVariable long pid) {
        return productService.getById(pid);
    }
    @PostMapping("/products")
    public ProductDTO addProduct(@RequestBody ProductAddDTO p, HttpServletRequest req){
        //todo fix
        long uid = getLoggedUserId(req);
        return productService.add(p);
    }

    @PostMapping("/products/{pid}/like")
    public LikedProductsDTO likeProduct(@PathVariable int pid, HttpServletRequest req){
//        todo check if logged v
         long uid = getLoggedUserId(req);
        return productService.like(pid,uid);
    }

    @PostMapping("/products/{pid}/cart")
    public ProductOrderDTO addProductInCart(@PathVariable int pid, HttpServletRequest req, @RequestParam int quantity){
        //todo check if logged v
        long uid = getLoggedUserId(req);
        return productService.addToCart(pid, uid, quantity);
    }

    @DeleteMapping("/products/{pid}/cart")
    public ProductOrderDTO removeProductFromCart(@PathVariable int pid, HttpServletRequest req){
        //todo check if logged v
        long uid = getLoggedUserId(req);
        return productService.removeProductFromCart(pid,uid);
    }

    @PostMapping("/products/{pid}/image")
    public String addImage(@RequestParam MultipartFile file, @PathVariable long pid, HttpServletRequest req){
        long uid = getLoggedUserId(req);
        //todo check if admin
        return productService.addImage(file,pid);
    }

    @PutMapping("products/{pid}")
    public ProductDTO editProduct(@PathVariable long pid,@RequestBody ProductAddDTO dto, HttpServletRequest req){
        //todo check if admin
        //todo
        long uid = getLoggedUserId(req);
        return productService.edit(pid, dto);
    }
    @DeleteMapping(value = "products/{pid}", headers = "password=dve")
    public ProductWithFeaturesDTO deleteProduct(@PathVariable long pid, HttpServletRequest req){
        //todo check if admin
        // check if ongoing purchases
        // cancel all purchases
        long uid = getLoggedUserId(req);
        return productService.deleteById(pid);
    }

    @PostMapping("products/{pid}/features/{fid}")
    public ProductWithFeaturesDTO addFeatureToProduct(@PathVariable int pid, @PathVariable int fid, @RequestParam String value){
        //todo check if admin
        return productService.addFeature(pid, fid, value);
    }

    @GetMapping("products/search/{word}")
    public List<ProductDTO> searchByWord(@PathVariable String word){
        return productService.searchByWord(word);
    }


    @DeleteMapping("products/{pid}/features/{fid}")
    public ProductFeatureDTO removeFeatureOfProduct(@PathVariable int pid, @PathVariable int fid){
        //todo check if admin
        return productService.deleteFeature(pid, fid);
    }


    @GetMapping("products")
    public List<ProductDTO> getAllProducts(@RequestParam(required = false) boolean sortByPrice,
                                           @RequestParam(required = false) boolean desc) {

        return productService.findAll(sortByPrice, desc);
    }

    @PutMapping("/products/{id}/discount")
    public ProductDTO editProductDiscount(@PathVariable long id,@RequestBody ProductAddDTO dto) {
        //todo check if admin
        return productService.editDiscount(id, dto);
    }

    @DeleteMapping("products/{pid}/images/{iid}")
    public String deleteImage(@PathVariable long pid, @PathVariable long iid, @RequestParam String url){
        //todo check if admin
      return productService.deleteImage(pid, iid, url);
    }

    @GetMapping("products/filter")
    public List<ProductQueryDTO> filterProducts(@RequestParam int min, @RequestParam int max,
                                                @RequestParam(required = false) boolean desc){
        return productService.filterMinMax(min, max, desc);
    }


}
