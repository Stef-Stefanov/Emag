package com.example.emag.service;

import com.example.emag.model.dto.category.CategoryDTO;
import com.example.emag.model.dto.category.CategoryRequestDTO;
import com.example.emag.model.dto.category.CategoryWithSubcategoryDTO;
import com.example.emag.model.dto.product.ProductDTO;
import com.example.emag.model.entities.Category;
import com.example.emag.model.entities.Discount;
import com.example.emag.model.entities.Product;
import com.example.emag.model.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService extends AbstractService{

    public CategoryDTO save(CategoryRequestDTO dto) {
        validateDTO(dto);
        Category category = new Category();
        if(dto.getParentCategoryId() != 0) {
            category.setParentCategory(getCategoryById(dto.getParentCategoryId()));
            Category parentCategory = getCategoryById(dto.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }
        category.setName(dto.getName().strip());
        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }

    private void validateDTO(CategoryRequestDTO dto){
        if(dto.getName().strip().length() < 2 || dto.getName().strip().length() > 255){
            throw new BadRequestException("Category name size is not valid");
        }
    }

    public CategoryDTO remove(long id) {
        Category category = getCategoryById(id);
        CategoryDTO dto = modelMapper.map(category, CategoryDTO.class);
        categoryRepository.deleteById(id);
        return dto;
    }

    public CategoryWithSubcategoryDTO getCategory(long id) {
        Category category = getCategoryById(id);
        return modelMapper.map(category, CategoryWithSubcategoryDTO.class);
    }

    public CategoryWithSubcategoryDTO edit(long id, CategoryRequestDTO dto) {
        validateDTO(dto);
        Category category = getCategoryById(id);
        category.setName(dto.getName().strip());
        if(dto.getParentCategoryId() != 0) {
            category.setParentCategory(getCategoryById(dto.getParentCategoryId()));
            Category parentCategory = getCategoryById(dto.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }
        return modelMapper.map(categoryRepository.save(category), CategoryWithSubcategoryDTO.class);
    }

    public List<ProductDTO> getAllProductsFromCategory(long id) {
        Category category = getCategoryById(id);
        List<Product> products = category.getProducts();
        return products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
    }

    public List<ProductDTO> setDiscountForCategory(long cid, long did) {
        Category category = getCategoryById(cid);
        Discount discount = getDiscountById(did);
        List<Product> products = category.getProducts();
        for (Product product : products) {
            product.setDiscount(discount);
        }
       return products.stream().map (product -> modelMapper.map(productRepository.save(product), ProductDTO.class))
                       .collect(Collectors.toList());
    }

    public List<CategoryWithSubcategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper
                .map(category, CategoryWithSubcategoryDTO.class)).collect(Collectors.toList());
    }
}
