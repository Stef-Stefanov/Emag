package com.example.emag.service;

import com.example.emag.model.dto.order.ProductOrderDTO;
import com.example.emag.model.dto.product.*;
import com.example.emag.model.entities.*;
import com.example.emag.model.exceptions.BadRequestException;
import com.example.emag.model.exceptions.NotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService extends AbstractService{

    public ProductWithFeaturesDTO getById(Long id){
        Product p = getProductById(id);
        return modelMapper.map(p,ProductWithFeaturesDTO.class);
    }

    public ProductWithFeaturesDTO deleteById(long pid) {
        Product p = getProductById(pid);
        ProductWithFeaturesDTO dto = modelMapper.map(p, ProductWithFeaturesDTO.class);
        productRepository.deleteById(pid);
        return dto;
    }

    public ProductDTO add(ProductAddDTO dto) {
        validateProduct(dto);
        Category category = getCategoryById(dto.getCategoryId());
        Product product = new Product();
        product.setName(dto.getName());
        product.setRegularPrice(dto.getRegularPrice());
        product.setDescription(dto.getDescription());
        product.setRegularPrice(dto.getRegularPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(category);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }



    public LikedProductsDTO like(long pid, long uid) {
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

    public ProductOrderDTO addToCart(long pid, long uid, int quantity) {
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
        ProductOrderDTO dto = new ProductOrderDTO();
        dto.setName(p.getName());
        dto.setQuantity(productsInCart.getQuantity());
        return dto;
    }

    public String addImage(MultipartFile file, long pid) {
        Product product = getProductById(pid);
        if(product.getProductImages().size() > 10){
            throw new BadRequestException("Max images for products is 10");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = "uploads" + File.separator + System.nanoTime() + "." + extension;
        File f = new File(name);
        if(!f.exists()){
            try {
                Files.copy(file.getInputStream(), f.toPath());
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        else{
          throw new BadRequestException("File already exists");
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
        return setProductProperties(dto, p);
    }

    private ProductDTO setProductProperties(ProductAddDTO dto, Product p) {
        Category category = getCategoryById(dto.getCategoryId());
        p.setCategory(category);
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setQuantity(dto.getQuantity());
        p.setRegularPrice(dto.getRegularPrice());
        return modelMapper.map(productRepository.save(p), ProductDTO.class);
    }

    public ProductOrderDTO removeProductFromCart(long pid, long uid) {
        UserProductsInCartKey pk = new UserProductsInCartKey();
        pk.setProductId(pid);
        pk.setUserId(uid);
        getUserCart(pk);
        userCartRepository.deleteById(pk);
        ProductOrderDTO dto = new ProductOrderDTO();
        Product product = getProductById(pid);
        dto.setName(product.getName());
        return dto;
    }

    public ProductWithFeaturesDTO addFeature(long pid, long fid, String value) {
        Product product = getProductById(pid);
        Feature feature = getFeatureById(fid);
        ProductFeatureKey pk = new ProductFeatureKey();
        pk.setProductId(pid);
        pk.setFeatureId(fid);
        ProductFeature productFeature = new ProductFeature();
        productFeature.setId(pk);
        productFeature.setFeature(feature);
        productFeature.setProduct(product);
        productFeature.setValue(value);
        modelMapper.map(productFeatureRepository.save(productFeature), ProductFeatureDTO.class);
        return modelMapper.map(product, ProductWithFeaturesDTO.class);

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

    public Page<ProductDTO> searchByWord(String word, Pageable pageable) {
        validateWord(word);
        List<Product> products = productRepository.
                findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(word.strip(), word.strip());
        int start = (int) pageable.getOffset();
        int end = (Math.min((start + pageable.getPageSize()), products.size()));
        Page<Product> productPage = new PageImpl<>(products.subList(start , end ) , pageable , products.size());
        if(products.isEmpty()){
            throw new NotFoundException("No products found");
        }
        return productPage.map(p -> modelMapper.map(p, ProductDTO.class));
    }

    private void validateWord(String word) {
        if(word.length() < 2){
            throw new BadRequestException("Invalid word, must enter 2 characters at least");
        }
    }

    public ProductFeatureDTO deleteFeature(long pid, long fid) {
        Product product = getProductById(pid);
        Feature feature = getFeatureById(fid);
        ProductFeatureKey pk = new ProductFeatureKey();
        pk.setProductId(pid);
        ProductFeature productFeature = new ProductFeature();
        productFeature.setId(pk);
        productFeature.setFeature(feature);
        productFeature.setProduct(product);
        pk.setFeatureId(fid);
        if(!product.getProductFeatures().contains(productFeatureRepository.getReferenceById(pk))) {
            throw new NotFoundException("Feature not found in current product");
        }
        productFeature.setValue(productFeatureRepository.getReferenceById(pk).getValue());
        productFeatureRepository.deleteById(pk);
        return modelMapper.map(productFeature, ProductFeatureDTO.class);

    }

    public Page<ProductDTO> findAll(Pageable pageable) {
        Sort sort = pageable.getSort();
        List<Product> products = productRepository.findAll(sort);
        int start = (int) pageable.getOffset();
        int end = (Math.min((start + pageable.getPageSize()), products.size()));
        Page<Product> productPage = new PageImpl<>(products.subList(start , end ) , pageable , products.size());
        return productPage.map(p -> modelMapper.map(p, ProductDTO.class));
    }

    public ProductDTO editDiscount(long productId, long discountId) {
        Product p = getProductById(productId);
        if(discountId == 0){
            p.setDiscount(null);
        }else {
            Discount discount = getDiscountById(discountId);
            p.setDiscount(discount);
            sendEmail(p);
        }
        productRepository.save(p);
        return modelMapper.map(p, ProductDTO.class);
    }

    public String deleteImage(long pid, long iid, String url) {
        Product p = getProductById(pid);
        ProductImage productImage = getProductImageById(iid);
        File file = new File("uploads" + File.separator + url);
        if(p.getProductImages().contains(productImage)){
            try {
                Files.delete(file.toPath());
                productImageRepository.delete(productImage);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        else {
            throw new BadRequestException("File does not exists");
        }
        return url;
    }

    public List<ProductQueryDTO> filterMinMax(int min, int max, boolean desc) {
        return productDAO.filterMinMaxPrice(min, max, desc);
    }
}
