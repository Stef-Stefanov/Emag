package com.example.emag.controller;

import com.example.emag.model.dto.category.CategoryDTO;
import com.example.emag.model.dto.category.CategoryRequestDTO;
import com.example.emag.model.dto.category.CategoryWithSubcategoryDTO;
import com.example.emag.model.dto.product.ProductDTO;
import com.example.emag.model.entities.Category;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.repositories.CategoryRepository;
import com.example.emag.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/categories")
    public CategoryDTO addCategory(@RequestBody CategoryRequestDTO category){
        //todo check if admin
        return categoryService.save(category);
    }

    @DeleteMapping(value = "/categories/{id}", headers = "password=dve")
    public CategoryDTO deleteCategory(@PathVariable long id){
        // todo check if admin
        return categoryService.remove(id);
    }

    @PutMapping("categories/{id}")
    public CategoryWithSubcategoryDTO editCategory(@PathVariable long id, @RequestBody CategoryRequestDTO dto){
        //todo check if admin
        return categoryService.edit(id, dto);
    }

    @GetMapping("categories/{id}")
    public List<ProductDTO> getAllProductsFromCategory(@PathVariable long id){
        return categoryService.getAllProductsFromCategory(id);
    }

    @GetMapping("categories")
    public List<CategoryWithSubcategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }
    @PostMapping("categories/{cid}/discount/{did}")
    public List<ProductDTO> setDiscountForCategory(@PathVariable long cid, @PathVariable long did){
        //todo check if admin
        return categoryService.setDiscountForCategory(cid, did);
    }
}
