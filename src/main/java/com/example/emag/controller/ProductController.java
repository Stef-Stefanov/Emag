package com.example.emag.controller;

import com.example.emag.model.dto.FeatureDTO;
import com.example.emag.model.dto.product.*;
import com.example.emag.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
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
        //todo check if logged
        int uid = getLoggedUserId(req);
        return productService.addToCart(pid, uid, quantity);
    }

    @DeleteMapping("/products/{pid}/cart")
    public int removeProductFromCart(@PathVariable int pid, HttpServletRequest req){
        //todo check if logged
        int uid = getLoggedUserId(req);
        return productService.removeProductFromCart(pid,uid);
    }

    @PostMapping("/products/{pid}/image")
    public String addPicture(@RequestParam MultipartFile file, @PathVariable long pid){
        //todo check if admin
        return productService.addImage(file,pid);
    }

    @PostMapping("products/{pid}")
    public ProductDTO editProduct(@PathVariable long pid,@RequestBody ProductAddDTO dto){
        //todo check if admin
        return productService.edit(pid, dto);
    }
    @DeleteMapping(value = "products/{pid}", headers = "password=dve")
    public ProductWithFeaturesDTO deleteProduct(@PathVariable long pid){
        //todo check if admin
        // check if ongoing purchases
        // cancel all purchases
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

    @GetMapping("products")
    public List<ProductDTO> getAllProducts(){
        return productService.getAllProducts();
    }
    //todo testing remove feature
    @DeleteMapping("products/{pid}/features/{fid}")
    public ProductFeatureDTO removeFeatureOfProduct(@PathVariable int pid, @PathVariable int fid){
        //todo check if admin
        return productService.deleteFeature(pid, fid);
    }


//    @GetMapping("/products")
//    public List<ProductDTO> getAllProductsByCategory(@RequestParam long category,
//                                        @RequestParam(required = false) boolean sortByPrice,
//                                        @RequestParam(required = false) boolean desc) {
//
//        return productService.findAllByCategoryId(category, sortByPrice, desc);
//    }

    //

}
