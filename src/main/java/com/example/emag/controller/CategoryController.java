package com.example.emag.controller;

import com.example.emag.model.dto.category.CategoryDTO;
import com.example.emag.model.entities.Category;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.repositories.CategoryRepository;
import com.example.emag.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private CategoryRepository repo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/categories")
    public Category addCategory(@RequestBody Category category){
        //todo check if name is valid
        // check other things
        return repo.save(category);
    }

    @GetMapping("/categories/{id}")
    public CategoryDTO getCategory(@PathVariable long id) {
        Category category = repo.findById(id).orElseThrow(() -> new NotFoundException("Category not found!"));
        return modelMapper.map(category,CategoryDTO.class);
    }

    @DeleteMapping(value = "/categories/{id}", headers = "password=dve")
    public void deleteCategory(@PathVariable long id){
        // todo check if admin
        // todo check if it exists
        repo.deleteById(id);
    }

}
