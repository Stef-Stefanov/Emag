package com.example.emag.model.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    // todo many to one self join
//    @ManyToOne
//    @JoinColumn(name="parent_category_id", insertable = false, updatable = false)
//    private Category parentCategory;

    @Column(name = "parent_category_id")
    private Long parentCategoryId;

//    @OneToMany(mappedBy="parentCategory")
//    private Set<Category> childCategories;
//    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)//Avoiding empty json arrays.objects
//    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
//    private List<Category> subCategory;
}
