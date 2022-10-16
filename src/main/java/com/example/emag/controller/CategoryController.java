package com.example.emag.controller;

import com.example.emag.model.dto.ErrorDTO;
import com.example.emag.model.entities.Category;
import com.example.emag.model.exceptions.NotFoundException;
import com.example.emag.model.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class CategoryController {

    @Autowired
    private CategoryRepository repo;

    @PostMapping("/categories")
    public Category addCategory(@RequestBody Category category){
        //todo check if name is valid
        // check other things
        repo.save(category);
        return category;
    }

    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable long id) {
        Optional<Category> category = repo.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorDTO handleException(Exception ex){
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg(ex.getMessage());
        dto.setStatus(HttpStatus.NOT_FOUND.value());
        dto.setTime(LocalDateTime.now());
        return dto;
    }

    @DeleteMapping(value = "/categories/{id}", headers = "password=dve")
    public void deleteCategory(@PathVariable long id){
        // todo check if admin
        // todo check if it exists
        repo.deleteById(id);
    }

}
