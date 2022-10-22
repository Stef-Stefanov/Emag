package com.example.emag.model.dto.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryWithSubcategoryDTO {
    private long id;
    private String name;
    private List<CategoryDTO> subCategories;
}
